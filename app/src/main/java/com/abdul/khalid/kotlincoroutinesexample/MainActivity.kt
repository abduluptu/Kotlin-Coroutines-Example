package com.abdul.khalid.kotlincoroutinesexample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val TAG: String = "KOTLIN_EXAMPLE"
    private lateinit var tvCounter: TextView

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tvCounter = findViewById(R.id.tvCounter)
        // Log.d(TAG, "${Thread.currentThread().name}")

        //Coroutines
        /*CoroutineScope(Dispatchers.Main).launch {
            task1()
        }

        CoroutineScope(Dispatchers.Main).launch {
            task2()
        }*/

        // launch() function
        /*val job = CoroutineScope(Dispatchers.IO).launch {
            printFollowers()
        }*/

        // job Hierarchy
        /*GlobalScope.launch(Dispatchers.Main) {
            // execute()
            execute2()
        }*/

        // withContext
        /* GlobalScope.launch {
             executeTask()
         }*/

        // runBlocking() function
        // main()

        // View Model
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Coroutines tied to the MainActivity
        lifecycleScope.launch {
            delay(2000) // 2 seconds
            val intent = Intent(this@MainActivity, AnotherActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    fun updateCounter(view: View) {
        //Log.d(TAG, "${Thread.currentThread().name}")
        tvCounter.text = "${tvCounter.text.toString().toInt() + 1}"
    }

    private fun executeLongRunningTask() {
        for (i in 1..1000000000L) {

        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun doAction(view: View) {
        /*thread(start = true){
            executeLongRunningTask()
        }*/
        CoroutineScope(Dispatchers.IO).launch {
            executeLongRunningTask()
            Log.d(TAG, "1-${Thread.currentThread().name}")
        }

        GlobalScope.launch(Dispatchers.Main) {
            Log.d(TAG, "2-${Thread.currentThread().name}")
        }

        MainScope().launch(Dispatchers.Default) {
            Log.d(TAG, "3-${Thread.currentThread().name}")
        }
    }

    private suspend fun task1() {
        Log.d(TAG, "Starting Task 1")
        // yield() //suspension point
        delay(1000) // 1 second
        Log.d(TAG, "Ending Task 1")
    }

    private suspend fun task2() {
        Log.d(TAG, "Starting Task 2")
        // yield()
        delay(2000) // 2 seconds
        Log.d(TAG, "Ending Task 2")
    }

    // using launch()
    /* private suspend fun printFollowers() {
         var fbFollowers = 0
         var instaFollowers = 0
         val job1 = CoroutineScope(Dispatchers.IO).launch {
             fbFollowers = getFbFollowers()
         }

         val job2 = CoroutineScope(Dispatchers.IO).launch {
             instaFollowers = getInstaFollowers()
         }
         // join will execute next line of code until coroutines task is executed.
         job1.join()
         job2.join()
         Log.d(TAG, "FB - $fbFollowers, Insta - $instaFollowers")
     }*/

    // using async()
    private suspend fun printFollowers() {
        /* val result = CoroutineScope(Dispatchers.IO).async {
             getFbFollowers()
         }
         Log.d(TAG, result.await().toString())*/

        /*val fb = CoroutineScope(Dispatchers.IO).async {
            getFbFollowers()
        }

        val insta = CoroutineScope(Dispatchers.IO).async {
            getInstaFollowers()
        }

        Log.d(TAG, "FB - ${fb.await()}, Insta - ${insta.await()}")*/

        // Common
        CoroutineScope(Dispatchers.IO).launch {
            val fb = async { getFbFollowers() }
            val insta = async { getInstaFollowers() }
            Log.d(TAG, "FB - ${fb.await()}, Insta - ${insta.await()}")
        }
    }

    // This function mimic an api
    private suspend fun getFbFollowers(): Int {
        delay(1000) // 1 second
        return 54
    }

    // This function mimic an api
    private suspend fun getInstaFollowers(): Int {
        delay(1000) // 1 second
        return 113
    }

    // job Hierarchy
    private suspend fun execute() {
        val parentJob = GlobalScope.launch(Dispatchers.Main) {
            // Log.d(TAG, "Parent - $coroutineContext")

            Log.d(TAG, "Parent Started")

            // Child Coroutines uses parent Coroutine coroutineContext
            /* val childJob = launch() {
                 Log.d(TAG, "Child - $coroutineContext")
             }*/

            // We can also define explicitly
            val childJob = launch(Dispatchers.IO) {
                // Log.d(TAG, "Child - $coroutineContext")

                /*Log.d(TAG, "Child Job Started")
                delay(5000) // 5 seconds
                Log.d(TAG, "Child Job Ended")*/

                // 5th
                try {
                    Log.d(TAG, "Child Job Started")
                    delay(5000) // 5 seconds
                    Log.d(TAG, "Child Job Ended")
                } catch (e: CancellationException) {
                    Log.d(TAG, "Child Job Cancelled")
                }

            }

            delay(3000) // 3 seconds
            //Log.d(TAG, "Child Job Cancelled")
            // 4th
            childJob.cancel()

            Log.d(TAG, "Parent Ended")
        }

        //3rd
        // delay(1000) // 1second
        // parentJob.cancel()

        parentJob.join()
        Log.d(TAG, "Parent Completed")
    }

    // 6th
    private suspend fun execute2() {
        val parentJob = CoroutineScope(Dispatchers.IO).launch {
            for (i in 1..1000) {
                /* executeLongRunningTask()
                 Log.d(TAG, i.toString())*/

                if (isActive) {
                    executeLongRunningTask()
                    Log.d(TAG, i.toString())
                }
            }
        }

        delay(100) // 0.1 second
        Log.d(TAG, "Cancelled Job")
        parentJob.cancel()
        parentJob.join()
        Log.d(TAG, "Parent Completed")
    }

    // withContext() function
    private suspend fun executeTask() {
        Log.d(TAG, "Before")

        // non-blocking (Coroutines)
        /*GlobalScope.launch {
            delay(1000) // 1 second
            Log.d(TAG, "Inside")
        }*/

        // blocking (Coroutines)
        withContext(Dispatchers.IO) {
            delay(1000) // 1 second
            Log.d(TAG, "Inside")
        }

        Log.d(TAG, "After")

    }

    // runBlocking() function
    private fun main() {
        // non-blocking (Coroutines)
        /*GlobalScope.launch {
            delay(1000)
            Log.d(TAG, "World")
        }
        Log.d(TAG, "Hello")
        Thread.sleep(2000)*/

        // blocking (Coroutines), It creates coroutines scope
        runBlocking {
            launch {
                delay(1000)
                Log.d(TAG, "World")
            }
            Log.d(TAG, "Hello")
        }
    }
}

package com.nfproject.quiz.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.nfproject.quiz.R
import com.nfproject.quiz.adapter.QuizAdapter
import com.nfproject.quiz.models.Quiz
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var adapter: QuizAdapter
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private var quizList = mutableListOf<Quiz>()
    lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
        popularDummyData()
    }

    private fun popularDummyData() {
//        quizList.add(Quiz("12-10-2021", "12-10-2021"))
//        quizList.add(Quiz("13-10-2021", "13-10-2021"))
//        quizList.add(Quiz("14-10-2021", "14-10-2021"))
//        quizList.add(Quiz("15-10-2021", "15-10-2021"))
//        quizList.add(Quiz("16-10-2021", "16-10-2021"))
//        quizList.add(Quiz("17-10-2021", "17-10-2021"))
//        quizList.add(Quiz("18-10-2021", "18-10-2021"))
//        quizList.add(Quiz("19-10-2021", "19-10-2021"))

    }

    fun setUpViews() {
        setUpFireStore()
        setUpDreawerLayout()
        setUpRecyclerView()
        setUpDataPicker()
    }

    private fun setUpDataPicker() {
        btnDataPicker.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)
                val dateFormatter = SimpleDateFormat("dd-mm-yyyy")
                val data = dateFormatter.format(Date(it))
                val intent = Intent(this, QuestionActivity::class.java)
                intent.putExtra("DATA", datePicker.headerText)
                startActivity(intent)
            }
            datePicker.addOnNegativeButtonClickListener {
                Log.d("DATEPICKER", datePicker.headerText)

            }
            datePicker.addOnCancelListener {
                Log.d("DATEPICKER", "Date Picker Cancelled")
            }
        }
    }

    private fun setUpFireStore() {
        firestore = FirebaseFirestore.getInstance()
        val collectionReference = firestore.collection("quizzes")
        collectionReference.addSnapshotListener { value, error ->

            if (value == null || error != null) {
                Toast.makeText(this, "Error fetching data", Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }
            Log.d("DATA", value.toObjects(Quiz::class.java).toString())
            quizList.clear()
            quizList.addAll(value.toObjects(Quiz::class.java))
            adapter.notifyDataSetChanged()
        }
    }

    private fun setUpRecyclerView() {
        adapter = QuizAdapter(this, quizList)
        quizRC.layoutManager = GridLayoutManager(this, 2)
        quizRC.adapter = adapter
    }

    fun setUpDreawerLayout() {
        setSupportActionBar(appBar)
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this, mainDrawer,
            R.string.app_name,
            R.string.app_name
        )
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
            mainDrawer.closeDrawers()
            true

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
package com.nfproject.quiz.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.nfproject.quiz.R
import com.nfproject.quiz.adapter.OptionAdapter
import com.nfproject.quiz.models.Question
import com.nfproject.quiz.models.Quiz
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {

    var quizzes: MutableList<Quiz>? = null
    var question: MutableMap<String, Question>? = null
    var index = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        setUpFirestore()

        setUpEventListener()
    }

    private fun setUpEventListener() {
        btnPrevius.setOnClickListener {
            index--
            bindViews()
        }
        btnNext.setOnClickListener {
            index++
            bindViews()
        }
        btmSubmit.setOnClickListener {
//            Toast.makeText(this, question.toString(), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, ResultActivity::class.java)
            val json = Gson().toJson(quizzes!![0])
            intent.putExtra("QUIZ", json)
            startActivity(intent)
        }
    }

    private fun setUpFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        var date = intent.getStringExtra("DATE")
        if (date != null) {
            firestore.collection("quizzes").whereEqualTo("title", date)
                .get()
                .addOnSuccessListener {
                    if (it != null && !it.isEmpty) {

                        quizzes = it.toObjects(Quiz::class.java)
                        question = quizzes!![0].question
                        bindViews()
                    }

                }
        }

    }

    private fun bindViews() {
        btnPrevius.visibility = View.GONE
        btmSubmit.visibility = View.GONE
        btnNext.visibility = View.GONE

        if (index == 1) {
            btnNext.visibility = View.VISIBLE
        } else if (index == question!!.size) {
            btmSubmit.visibility = View.VISIBLE
            btnPrevius.visibility = View.VISIBLE
        } else {
            btnPrevius.visibility = View.VISIBLE
            btnNext.visibility = View.VISIBLE
        }

        val question = question!!["question$index"]
        question?.let {
            description.text = question.description
            val optionAdapter = OptionAdapter(this, it)
            optionList.layoutManager = LinearLayoutManager(this)
            optionList.adapter = optionAdapter
            optionList.setHasFixedSize(true)
        }

    }
}
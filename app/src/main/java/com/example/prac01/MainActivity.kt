package com.example.prac01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prac01.databinding.ActivityMainBinding


var drawMode: Int = -1

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {
            drawMode = 0
            startActivity(Intent(this@MainActivity, DrawSurface::class.java))
        }
        binding.button2.setOnClickListener {
            drawMode = 1
            startActivity(Intent(this@MainActivity, DrawTriangle::class.java))
        }
        binding.button3.setOnClickListener {
            drawMode = 2
            startActivity(Intent(this@MainActivity, DrawSquare::class.java))
        }
        binding.button4.setOnClickListener {
            drawMode = 3
            startActivity(Intent(this@MainActivity, DrawColorCube::class.java))
        }
        binding.button5.setOnClickListener {
            drawMode = 4
            startActivity(Intent(this@MainActivity, DrawHexapyramid::class.java))
        }
    }
}
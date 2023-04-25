package com.example.prac01

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prac01.databinding.ActivityDrawObjectsBinding

class DrawObjects : AppCompatActivity() {
    val binding: ActivityDrawObjectsBinding by lazy {
        ActivityDrawObjectsBinding.inflate(layoutInflater)
    }

    var isRotating = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        initGLSurfaceView()
        setContentView(binding.root)

        binding.rotateX.setOnClickListener {
            rotateAxis = 0
        }

        binding.rotateY.setOnClickListener {
            rotateAxis = 1
        }

        binding.rotateZ.setOnClickListener {
            rotateAxis = 2
        }

        binding.toggleBtn.setOnClickListener {
            if (isRotating) {
                binding.toggleBtn.text = "Start"
            }
            else {
                binding.toggleBtn.text = "Stop"
            }
        }

    }

    fun initGLSurfaceView() {
        binding.surfaceView.setEGLContextClientVersion(3)
        binding.surfaceView.setRenderer(MainGLRenderer())
        binding.surfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }
}
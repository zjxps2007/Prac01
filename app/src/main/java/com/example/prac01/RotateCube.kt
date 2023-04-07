package com.example.prac01

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prac01.databinding.ActivityRotateCubeBinding

var rotateAxis = 0

class RotateCube : AppCompatActivity() {
    val binding: ActivityRotateCubeBinding by lazy {
        ActivityRotateCubeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_rotate_cube)
        intiGLSurfaceView()
        setContentView(binding.root)

        binding.rotateX.setOnClickListener {
            rotateAxis = 0
        }

        binding.rotateX.setOnClickListener {
            rotateAxis = 1
        }

        binding.rotateX.setOnClickListener {
            rotateAxis = 2
        }
    }

    fun intiGLSurfaceView() {
        binding.surfaceView.setEGLContextClientVersion(3)

        binding.surfaceView.setRenderer(MainGLRenderer())

//        binding.surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        binding.surfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }
}
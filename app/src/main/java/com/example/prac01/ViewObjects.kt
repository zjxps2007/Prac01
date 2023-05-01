package com.example.prac01

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prac01.databinding.ActivityViewObjectsBinding

var viewMode = 0

class ViewObjects : AppCompatActivity() {
    val binding: ActivityViewObjectsBinding by lazy {
        ActivityViewObjectsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        //setContentView(R.layout.activity_view_objects)

        initSurfaceView()
        setContentView(binding.root)

        binding.ortho.setOnClickListener {
            viewMode = 1
            binding.surfaceView.requestRender()
        }

        binding.persp?.setOnClickListener {
            viewMode = 0
            binding.surfaceView.requestRender()
        }
    }

    fun initSurfaceView() {
        binding.surfaceView.setEGLContextClientVersion(3)

        binding.surfaceView.setRenderer(MainGLRenderer())

        binding.surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}
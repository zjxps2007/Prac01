package com.example.prac01

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prac01.databinding.ActivityRotateCubeBinding

var rotateAxis = 0
var scaleFactor = 1f
var displace = floatArrayOf(0f, 0f, 0f)

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

        binding.rotateY.setOnClickListener {
            rotateAxis = 1
        }

        binding.rotateZ.setOnClickListener {
            rotateAxis = 2
        }

        binding.ScaleUp.setOnClickListener {
            scaleFactor *= 1.1f
        }

        binding.ScaleDown.setOnClickListener {
            scaleFactor *= 0.9f
        }

        binding.poX.setOnClickListener {
            displace[0] += 0.1f
        }

        binding.negX.setOnClickListener {
            displace[0] -= 0.1f
        }

        binding.poY.setOnClickListener {
            displace[1] += 0.1f
        }

        binding.negY.setOnClickListener {
            displace[1] -= 0.1f
        }

        binding.poZ.setOnClickListener {
            displace[2] += 0.1f
        }

        binding.negZ.setOnClickListener {
            displace[2] -= 0.1f
        }
    }

    fun intiGLSurfaceView() {
        binding.surfaceView.setEGLContextClientVersion(3)

        binding.surfaceView.setRenderer(MainGLRenderer(this))

//        binding.surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
        binding.surfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
    }
}
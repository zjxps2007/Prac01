package com.example.prac01

import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.prac01.databinding.ActivityViewObjectsBinding

var viewOrtho = false
var eyePos = floatArrayOf(2.0f, 2.0f, 2.0f)
var eyeAt = floatArrayOf(0.0f, 0.0f, 0.0f)
var cameraVec = floatArrayOf(-0.57735f, -0.57735f, -0.57735f)

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
            viewOrtho = true
            binding.surfaceView.requestRender()
        }

        binding.persp.setOnClickListener {
            viewOrtho = false
            binding.surfaceView.requestRender()
        }

        val sinTheta = 0.17365f
        val cosTheta = 0.98481f

        binding.eyeLeft.setOnClickListener {
            val newVecZ = cosTheta * cameraVec[2] - sinTheta * cameraVec[0]
            val newVecX = sinTheta * cameraVec[2] + cosTheta * cameraVec[0]
            cameraVec[0] = newVecX
            cameraVec[2] = newVecZ
            binding.surfaceView.requestRender()
        }

        binding.eyeRight.setOnClickListener {
            val newVecZ = cosTheta * cameraVec[2] + sinTheta * cameraVec[0]
            val newVecX = -sinTheta * cameraVec[2] + cosTheta * cameraVec[0]
            cameraVec[0] = newVecX
            cameraVec[2] = newVecZ
            binding.surfaceView.requestRender()
        }

        binding.eyeForward.setOnClickListener {
            val newPosX = eyePos[0] + 0.5f * cameraVec[0]
            val newPosZ = eyePos[2] + 0.5f * cameraVec[2]
            if (newPosX > -10 && newPosX < 10 && newPosZ > -10 && newPosZ < 10) {
                eyePos[0] = newPosX
                eyePos[2] = newPosZ
                binding.surfaceView.requestRender()
            }
        }

        binding.eyeBackward.setOnClickListener {
            val newPosX = eyePos[0] - 0.5f * cameraVec[0]
            val newPosZ = eyePos[2] - 0.5f * cameraVec[2]
            if (newPosX > -10 && newPosX < 10 && newPosZ > -10 && newPosZ < 10) {
                eyePos[0] = newPosX
                eyePos[2] = newPosZ
                binding.surfaceView.requestRender()
            }
        }
    }

    fun initSurfaceView() {
        binding.surfaceView.setEGLContextClientVersion(3)

        binding.surfaceView.setRenderer(MainGLRenderer())

        binding.surfaceView.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}
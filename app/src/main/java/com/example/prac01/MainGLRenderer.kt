package com.example.prac01

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainGLRenderer: GLSurfaceView.Renderer {

    private lateinit var mTriangle: MyTriangle
    private lateinit var mSquare: MySquare
    private lateinit var mCube: MyColorCube

    private var projectionMatrix = FloatArray(16)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        Matrix.setIdentityM(projectionMatrix, 0)

        when (drawMode) {
            1 -> mTriangle = MyTriangle()
            2 -> mSquare = MySquare()
            3 -> mCube = MyColorCube()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

        when (drawMode) {
            3 -> {
                if (width > height) {
                    val ratio = width.toFloat() / height.toFloat()
                    Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 0f, 1000f)
                }
                else {
                    val ratio = height.toFloat() / width.toFloat()
                    Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, 0f, 1000f)
                }
            }
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        when (drawMode) {
            1 -> mTriangle.draw()
            2 -> mSquare.draw()
            3 -> mCube.draw(projectionMatrix)
        }
    }
}
package com.example.prac01

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MainGLRenderer: GLSurfaceView.Renderer {

    private lateinit var mTriangle: MyTriangle
    private lateinit var mSquare: MySquare

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        when (drawMode) {
            1 -> mTriangle = MyTriangle()
            2 -> {
                mTriangle = MyTriangle()
                mSquare = MySquare()
            }
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        when (drawMode) {
            1 -> mTriangle.draw()
            2 -> {
                mSquare.draw()
                mTriangle.draw()
            }
        }
    }
}
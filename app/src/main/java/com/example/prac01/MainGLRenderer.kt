package com.example.prac01

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import android.os.SystemClock
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.math.cos
import kotlin.math.sin

class MainGLRenderer: GLSurfaceView.Renderer {

    private lateinit var mTriangle: MyTriangle
    private lateinit var mSquare: MySquare
    private lateinit var mCube: MyColorCube
    private lateinit var mHexagonal: MyHexapyramid

    private var mvpMatrix = FloatArray(16)
    private var viewMatrix = FloatArray(16)
    private var projectionMatrix = FloatArray(16)
    private var vpMatrix = FloatArray(16)
    private var modelMatrix = floatArrayOf(
        1f, 0f, 0f, 0f,
        0f, 1f, 0f, 0f,
        0f, 0f, 1f, 0f,
        0f, 0f, 0f, 1f
    )

    private var startTime = SystemClock.uptimeMillis()
    private var rotAngles = floatArrayOf(0f, 0f, 0f)

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        GLES30.glEnable(GLES30.GL_DEPTH_TEST)

        Matrix.setIdentityM(mvpMatrix, 0)
        Matrix.setIdentityM(viewMatrix, 0)
        Matrix.setIdentityM(projectionMatrix, 0)
        Matrix.setIdentityM(vpMatrix, 0)

        when (drawMode) {
            1 -> mTriangle = MyTriangle()
            2 -> mSquare = MySquare()
            3, 5 -> mCube = MyColorCube()
            4 -> mHexagonal = MyHexapyramid()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

        when (drawMode) {
            3, 5 -> {
//                if (width > height) {
//                    val ratio = width.toFloat() / height.toFloat()
//                    Matrix.orthoM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 0f, 1000f)
//                }
//                else {
//                    val ratio = height.toFloat() / width.toFloat()
//                    Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, 0f, 1000f)
//                }

                val ratio: Float = width.toFloat() / height.toFloat()
                Matrix.perspectiveM(projectionMatrix, 0, 90f, ratio, 0.0001f, 1000f)
                Matrix.setLookAtM(viewMatrix, 0, 1f, 1f, 1f, 0f, 0f, 0f,0f,1.0f, 0f)
//                Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

                Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

            }

            4 -> {
                val ratio: Float = width.toFloat() / height.toFloat()
                Matrix.perspectiveM(projectionMatrix, 0, 90f, ratio, 0.0001f, 1000f)
                Matrix.setLookAtM(viewMatrix, 0, 0f, 2f, 2.5f, 0f, 0f, 0f,0f,1.0f, 0f)
                Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
            }
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        when (drawMode) {
            1 -> mTriangle.draw()
            2 -> mSquare.draw()
            3 -> mCube.draw(vpMatrix)
            4 -> mHexagonal.draw(vpMatrix)
            5 -> {
                val angle = 0.001f * (SystemClock.uptimeMillis() - startTime.toFloat())
                val sinAngle = sin(angle)
                val cosAngle = cos(angle)
                if (rotateAxis == 0) {
                    modelMatrix = floatArrayOf(
                        1f, 0f, 0f, 0f,
                        0f, cosAngle, -sinAngle, 0f,
                        0f, sinAngle, cosAngle, 0f,
                        0f, 0f, 0f, 1f,
                    )
                }
                else if (rotateAxis == 1) {
                    modelMatrix = floatArrayOf(
                        cosAngle, 0f, sinAngle, 0f,
                        0f, 1f, 0f, 0f,
                        -sinAngle, 0f, cosAngle, 0f,
                        0f, 0f, 0f, 1f
                    )
                }
                else if (rotateAxis == 2) {
                    modelMatrix = floatArrayOf(
                        cosAngle, -sinAngle, 0f, 0f,
                        sinAngle, cosAngle, 0f, 0f,
                        0f, 0f, 1f, 0f,
                        0f, 0f, 0f, 1f
                    )
                }

                Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix, 0)

                mCube.draw(mvpMatrix)
            }
        }
    }
}
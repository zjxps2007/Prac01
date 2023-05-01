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
    private var aspectRatio = 1.0f

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
            3, 5, 7 -> mCube = MyColorCube()
            4, 6 -> mHexagonal = MyHexapyramid()
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)

        aspectRatio = width.toFloat() / height.toFloat()

        when (drawMode) {
            3, 4, 7 -> {
                val ratio = width.toFloat() / height.toFloat();
                Matrix.perspectiveM(projectionMatrix, 0, 90f, ratio, 0.001f, 1000f)
            }
            /*
            3 -> {
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

             */

            5, 6 -> {
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

        when (drawMode) {
            3, 5, 7 -> {
                Matrix.setLookAtM(viewMatrix, 0, 1f, 1f, 1f, 0f, 0f, 0f, 0f, 1f, 0f)
            }
            4 -> {
                Matrix.setLookAtM(viewMatrix, 0, 2f, 2f, 2f, 0f, 0f, 0f, 0f, 1f, 0f)
            }

            6 -> {
                Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 2f, 0f, 0f, 0f, 0f, 1f, 0f)
            }
        }

        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
    }



    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        when (drawMode) {
            5 -> {
                Matrix.setIdentityM(modelMatrix, 0)
//                val angle = 0.001f * (SystemClock.uptimeMillis() - startTime.toFloat())
                val endTime = SystemClock.uptimeMillis()
                val angle = 0.001f * (endTime - startTime).toFloat()
                startTime = endTime
                rotAngles[rotateAxis] += angle
                var sinAngle = sin(rotAngles[0])
                var cosAngle = cos(rotAngles[0])
                val rotXMatrix = floatArrayOf(
                    1f, 0f, 0f, 0f,
                    0f, cosAngle, sinAngle, 0f,
                    0f, -sinAngle, cosAngle, 0f,
                    0f, 0f, 0f, 1f,
                )

                sinAngle = sin(rotAngles[1])
                cosAngle = cos(rotAngles[1])
                val rotYMatrix = floatArrayOf(
                    cosAngle, 0f, -sinAngle, 0f,
                    0f, 1f, 0f, 0f,
                    sinAngle, 0f, cosAngle, 0f,
                    0f, 0f, 0f, 1f
                )

                sinAngle = sin(rotAngles[2])
                cosAngle = cos(rotAngles[2])
                val rotZMatrix = floatArrayOf(
                    cosAngle, sinAngle, 0f, 0f,
                    -sinAngle, cosAngle, 0f, 0f,
                    0f, 0f, 1f, 0f,
                    0f, 0f, 0f, 1f
                )

                Matrix.multiplyMM(modelMatrix, 0, rotYMatrix, 0, rotXMatrix, 0)
                Matrix.multiplyMM(modelMatrix, 0, rotZMatrix, 0, modelMatrix, 0)

                val scaleMatrix = floatArrayOf(
                    scaleFactor, 0f, 0f, 0f,
                    0f, scaleFactor, 0f, 0f,
                    0f, 0f, scaleFactor, 0f,
                    0f, 0f, 0f, 1f
                )
                Matrix.multiplyMM(modelMatrix, 0, modelMatrix, 0, scaleMatrix, 0)

                val translateMatrix = floatArrayOf(
                    1f, 0f, 0f, 0f,
                    0f, 1f, 0f, 0f,
                    0f, 0f, 1f, 0f,
                    displace[0], displace[1], displace[2], 1f
                )
                Matrix.multiplyMM(modelMatrix, 0, translateMatrix, 0, modelMatrix, 0)
            }
            6 -> {
                Matrix.setIdentityM(modelMatrix, 0)

                val endTime = SystemClock.uptimeMillis()
                val angle = 0.01f * (endTime - startTime). toInt()
                startTime = endTime
                rotAngles[rotateAxis] += angle

                Matrix.setRotateM(modelMatrix, 0, rotAngles[0], 1f, 0f, 0f)
                val tempMatrix = floatArrayOf(1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f)
                Matrix.setRotateM(modelMatrix, 0, rotAngles[1], 0f, 1f, 0f)
                Matrix.multiplyMM(modelMatrix, 0, tempMatrix, 0, modelMatrix, 0)
                Matrix.setRotateM(modelMatrix, 0, rotAngles[2], 0f, 0f, 1f)
                Matrix.multiplyMM(modelMatrix, 0, tempMatrix, 0, modelMatrix, 0)
            }
        }

        Matrix.multiplyMM(mvpMatrix, 0, vpMatrix, 0, modelMatrix, 0)

        when (drawMode) {
            1 -> mTriangle.draw()
            2 -> mSquare.draw()
            3 -> mCube.draw(vpMatrix)
            4 -> mHexagonal.draw(vpMatrix)
            5 -> mCube.draw(mvpMatrix)
            6 -> mHexagonal.draw(mvpMatrix)

            7 -> {
                if (viewMode == 0) {
                    Matrix.perspectiveM(projectionMatrix, 0, 90f, aspectRatio, 0.001f, 1000f)
                }
                else {
                    if (aspectRatio >= 1.0f) {
                        Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 0f, 1000f)
                    }
                    else {
                        val ratio = 1.0f / aspectRatio
                        Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -ratio, ratio, 0f, 1000f)
                    }
                }

                Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

                mCube.draw(vpMatrix)
            }
        }
    }
}
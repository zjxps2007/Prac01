package com.example.prac01

import android.opengl.GLES30
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

class MyColorCube {
    private val vertexCoords = floatArrayOf(
        -0.5f, 0.5f, -0.5f,
        -0.5f, -0.5f, -0.5f,
        0.5f, -0.5f, -0.5f,
        0.5f, 0.5f, -0.5f,
        -0.5f, 0.5f, 0.5f,
        -0.5f, -0.5f, 0.5f,
        0.5f, -0.5f, 0.5f,
        0.5f, 0.5f, 0.5f,
    )

    private val vertexColors = floatArrayOf(
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f,
        1.0f, 0.0f, 0.0f,
        1.0f, 1.0f, 0.0f,
        0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 1.0f,
    )

    private val drawOrder = shortArrayOf(
        0, 3, 2, 0, 2, 1,
        2, 3, 7, 2, 7, 6,
        1, 2, 6, 1, 6, 5,
        4, 0, 1, 4, 1, 5,
        3, 0, 4, 3, 4, 7,
        5, 6, 7, 5, 7, 4

    )

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexCoords.size * 4).run {
            order(ByteOrder.nativeOrder())

            asFloatBuffer().apply {
                put(vertexCoords)
                position(0)
            }
        }

    private var colorBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexColors.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertexColors)
                position(0)
            }
        }

    private val indexBuffer: ShortBuffer =
        ByteBuffer.allocateDirect(drawOrder.size * 2).run {
            order(ByteOrder.nativeOrder())
            asShortBuffer().apply {
                put(drawOrder)
                position(0)
            }
        }

    private val vertexShaderCode =
        "#version 300 es\n" +
                "uniform mat4 uMVPMatrix;\n" +
                "layout(location = 3) in vec4 vPosition;\n" +
                "layout(location = 4) in vec4 vColor;\n" +
                "out vec4 fColor;\n" +
                "void main(){\n" +
                "gl_Position = uMVPMatrix * vPosition;\n" +
                "fColor = vColor;\n" +
                "}\n"

    private val fragmentShaderCode =
        "#version 300 es\n" +
                "precision mediump float;\n" +
                "in vec4 fColor;\n" +
                "out vec4 fragColor;\n" +
                "void main(){\n" +
                "fragColor = fColor;\n" +
                "}\n"

    private var mProgram: Int = -1

    private var mvpMatrixHandle: Int = -1

    private val vertexStride: Int = COORDS_PER_VERTEX * 4

    init {
        val vertexShader: Int = loadShader(GLES30.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShaderCode)

        mProgram = GLES30.glCreateProgram().also {
            GLES30.glAttachShader(it, vertexShader)

            GLES30.glAttachShader(it, fragmentShader)

            GLES30.glLinkProgram(it)
        }

        GLES30.glUseProgram(mProgram)

        GLES30.glEnableVertexAttribArray(3)
        GLES30.glVertexAttribPointer(
            3,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )

        GLES30.glEnableVertexAttribArray(4)
        GLES30.glVertexAttribPointer(
            4,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            vertexStride,
            colorBuffer
        )

        mvpMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")
    }

    private fun loadShader(type: Int, shaderCode: String): Int {
        return GLES30.glCreateShader(type).also { shader->

            GLES30.glShaderSource(shader, shaderCode)
            GLES30.glCompileShader(shader)

            val compiled = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer()
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled)
            if (compiled.get(0) == 0) {
                GLES30.glGetShaderiv(shader, GLES30.GL_INFO_LOG_LENGTH, compiled)
                if (compiled.get(0) > 1) {
                    Log.e("Shader", "$type shader: " + GLES30.glGetShaderInfoLog(shader))
                }
                GLES30.glDeleteShader(shader)
                Log.e("Shader", "$type shader compile error.")
            }
        }
    }

    fun draw(mvpMatrix: FloatArray) {
        GLES30.glUseProgram(mProgram)

        GLES30.glUniformMatrix4fv(mvpMatrixHandle, 1, false, mvpMatrix, 0)

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrder.size, GLES30.GL_UNSIGNED_SHORT, indexBuffer)
    }
}
package com.example.prac01

import android.opengl.GLES30
import android.util.Log
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MyGround {

    private val vertexCoords = floatArrayOf(
        -10.0f, -1.0f, -10.0f,
        -10.0f, -1.0f, 10.0f,
        10.0f, -1.0f, 10.0f,
        -10.0f, -1.0f, -10.0f,
        10.0f, -1.0f, 10.0f,
        10.0f, -1.0f, -10.0f,

        0.0f, -1.0f, -10.0f,
        0.0f, -1.0f, 10.0f,
        -10.0f, -1.0f, 0.0f,
        10.0f, -1.0f, 0.0f,
    )

    private val lineCoords = FloatArray(252).apply {
        var index = 0
        for (x in -10 .. 10) {
            this[index++] = x.toFloat()
            this[index++] = -1.0f
            this[index++] = -10.0f
            this[index++] = x.toFloat()
            this[index++] = -1.0f
            this[index++] = 10.0f
        }
        for (z in -10 .. 10) {
            this[index++] = -10.0f
            this[index++] = -1.0f
            this[index++] = z.toFloat()
            this[index++] = 10.0f
            this[index++] = -1.0f
            this[index++] = z.toFloat()
        }
    }

    private val color = floatArrayOf(0.8f, 0.8f, 0.8f, 1.0f)

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect((vertexCoords.size + lineCoords.size) * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertexCoords)
                put(lineCoords)
                position(0)
            }
        }

    private val vertexShaderCode =
        "#version 300 es\n" +
                "uniform mat4 uMVPMatrix;" +
                "layout(location = 7) in vec4 vPosition;\n" +
                "void main(){\n" +
                "gl_Position = uMVPMatrix * vPosition;\n" +
                "}\n"

    private val fragmentShaderCode =
        "#version 300 es\n" +
                "precision mediump float;\n" +
                "uniform vec4 fColor;\n" +
                "out vec4 fragColor;\n" +
                "void main(){\n" +
                "fragColor = fColor;\n" +
                "}\n"

    private var mProgram: Int = -1
    private var mvpMatrixHandle: Int = -1
    private var mColorHandle: Int = -1

    private val lineCount: Int = lineCoords.size / COORDS_PER_VERTEX
    private val vertexCount: Int = vertexCoords.size / COORDS_PER_VERTEX
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

        //mPositionHandle = GLES30.glGetAttribLocation(mProgram, "vPosition").also {
        GLES30.glEnableVertexAttribArray(7)

        GLES30.glVertexAttribPointer(
            7,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )
        //}

        mColorHandle = GLES30.glGetUniformLocation(mProgram, "fColor").also {
            GLES30.glUniform4fv(it, 1, color, 0)
        }

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

        GLES30.glUniform4fv(mColorHandle, 1, color, 0)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)

        GLES30.glLineWidth(5.0f)
        GLES30.glUniform4f(mColorHandle, 0f, 0f, 0f, 1f)
        GLES30.glDrawArrays(GLES30.GL_LINES, vertexCount, lineCount)
    }
}
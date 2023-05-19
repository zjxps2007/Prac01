package com.example.prac01

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES30
import android.opengl.GLUtils
import android.util.Log
import java.io.BufferedInputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class MyTexGround(private val myContext: Context) {
    private val vertexCoords = floatArrayOf(
        -10.0f, -1.0f, -10.0f,
        -10.0f, -1.0f, 10.0f,
        10.0f, -1.0f, 10.0f,
        -10.0f, -1.0f, -10.0f,
        10.0f, -1.0f, 10.0f,
        10.0f, -1.0f, -10.0f,
    )

    private val vertexUVs = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 20.0f,
        20.0f, 20.0f,
        0.0f, 0.0f,
        20.0f, 20.0f,
        20.0f, 0.0f
    )

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexCoords.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertexCoords)
                position(0)
            }
        }

    private var uvBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(vertexUVs.size * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(vertexUVs)
                position(0)
            }
        }


    private val vertexShaderCode =
        "#version 300 es\n" +
                "uniform mat4 uMVPMatrix;\n" +
                "layout(location = 10) in vec4 vPosition;\n" +
                "layout(location = 11) in vec2 vTexCoord;\n" +
                "out vec2 fTexCoord; \n" +
                "void main(){\n" +
                "gl_Position = uMVPMatrix * vPosition;\n" +
                "fTexCoord = vTexCoord; \n" +
                "}\n"

    private val fragmentShaderCode =
        "#version 300 es\n" +
                "precision mediump float;\n" +
                "uniform sampler2D sTexture;\n" +
                "in vec2 fTexCoord;\n" +
                "out vec4 fragColor;\n" +
                "void main(){\n" +
                "fragColor = texture(sTexture, fTexCoord);\n" +
                "}\n"

    private var mProgram: Int = -1
    private var mvpMatrixHandle: Int = -1
    private var textureID = IntArray(1)

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


        GLES30.glEnableVertexAttribArray(10)

        GLES30.glVertexAttribPointer(
            10,
            COORDS_PER_VERTEX,
            GLES30.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )

        GLES30.glEnableVertexAttribArray(11)
        GLES30.glVertexAttribPointer(
            11,
            2,
            GLES30.GL_FLOAT,
            false,
            0,
            uvBuffer
        )

        mvpMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix")

        GLES30.glGenTextures(1, textureID, 0)

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0)
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID[0])
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR_MIPMAP_NEAREST)
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR)
//        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_MIRRORED_REPEAT)
//        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_MIRRORED_REPEAT)
        GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, loadBitmap("logo.bmp"), 0)
        GLES30.glGenerateMipmap(GLES30.GL_TEXTURE_2D)
    }

    private fun loadBitmap(filename: String): Bitmap {
        val manager = myContext.assets
        val inputStream = BufferedInputStream(manager.open(filename))
        val bitmap: Bitmap? = BitmapFactory.decodeStream(inputStream)
        return bitmap!!
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
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureID[0])

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertexCount)
    }
}
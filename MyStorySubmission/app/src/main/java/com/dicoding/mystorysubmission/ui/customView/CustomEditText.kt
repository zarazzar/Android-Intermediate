package com.dicoding.mystorysubmission.ui.customView

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import com.google.android.material.textfield.TextInputEditText
import android.text.TextWatcher
import com.dicoding.mystorysubmission.R
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import android.util.Patterns

class CustomEditText : TextInputEditText {

    private var isError: Boolean = false
    private var warningBg: Drawable? = null
    private var normalBg: Drawable? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        normalBg = ContextCompat.getDrawable(context, R.drawable.bg_edt_normal)
        warningBg = ContextCompat.getDrawable(context, R.drawable.bg_edt_warning)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val input = p0.toString()
                when (inputType) {
                    Email -> {
                        if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                            error = context.getString(R.string.validate_email)
                            isError = true
                        } else {
                            isError = false
                        }
                    }

                    Password -> {
                        isError = if (input.length < 8) {
                            setError(context.getString(R.string.validate_password), null)
                            true
                        } else {
                            false
                        }
                    }

                    else -> {
                        isError = if (input.isEmpty()) {
                            setError(context.getString(R.string.validate_password), null)
                            true
                        } else {
                            false
                        }
                    }
                }
            }


            override fun afterTextChanged(p0: Editable?) {
                val input = p0.toString()
                when (inputType) {
                    Email -> {
                        if (!Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                            error = context.getString(R.string.validate_email)
                            isError = true
                        } else {
                            isError = false
                        }
                    }

                    Password -> {
                        isError = if (input.length < 8) {
                            setError(context.getString(R.string.validate_password), null)
                            true
                        } else {
                            false
                        }
                    }

                    else -> {
                        isError = if (input.isEmpty()) {
                            setError(context.getString(R.string.empty_warning), null)
                            true
                        } else {
                            false
                        }
                    }
                }
            }
        })
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        background = if (isError) {
            warningBg
        } else {
            normalBg
        }
    }

    companion object {
        const val Email = 0x00000021
        const val Password = 0x00000081
    }
}


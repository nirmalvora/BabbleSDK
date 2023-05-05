package com.babble.babblesdk.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.babble.babblesdk.BabbleSDKController
import com.babble.babblesdk.R
import com.babble.babblesdk.customWidgets.BabbleDynamicSquare
import com.babble.babblesdk.model.questionsForUser.UserQuestionResponse
import com.babble.babblesdk.utils.BabbleGenericClickHandler
import com.babble.babblesdk.utils.BabbleSdkHelper

internal class BabbleSurveyAdapter(
    mContext: Context,
    surveyFields: UserQuestionResponse, babbleClickHandler: BabbleGenericClickHandler,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mContext: Context
    private val mInflater: LayoutInflater
    private var babbleClickHandler: BabbleGenericClickHandler
    private var listSize = 0
    private var viewType = -1
    private var surveyFields: UserQuestionResponse? = null

    private val emojis = arrayOf(
        R.string.smileyHtml1,
        R.string.smileyHtml2,
        R.string.smileyHtml3,
        R.string.smileyHtml4,
        R.string.smileyHtml5
    )

    class MCQRadioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: RadioButton

        init {
            title = view.findViewById<View>(R.id.child_title) as RadioButton
        }
    }

    class RatingsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView

        init {
            title = view.findViewById<View>(R.id.ratings_list_child_tv) as TextView
        }
    }

    class RatingsStarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var stars: ImageView

        init {
            stars = view.findViewById<View>(R.id.ratings_list_child_image_view) as ImageView
        }
    }

    class RatingsEmojiViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var emoji: TextView

        init {
            emoji = view.findViewById<View>(R.id.ratings_list_child_tv) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        return when (viewType) {
            0 -> {
                view = mInflater.inflate(R.layout.mcq_radio_list_child, parent, false)
                MCQRadioViewHolder(view)
            }

            1 -> {
                view = mInflater.inflate(R.layout.ratings_list_child, parent, false)
                RatingsViewHolder(view)
            }

            2 -> {
                view = mInflater.inflate(R.layout.ratings_star_list_child, parent, false)
                RatingsStarViewHolder(view)
            }

            3 -> {
                view = mInflater.inflate(R.layout.ratings_emoji_child, parent, false)
                RatingsEmojiViewHolder(view)
            }

            else -> {
                view = mInflater.inflate(R.layout.mcq_radio_list_child, parent, false)
                MCQRadioViewHolder(view)
            }

        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (viewType) {
            0 -> {
                val mcqHolder: MCQRadioViewHolder = holder as MCQRadioViewHolder
                mcqHolder.title.text =
                    this.surveyFields?.document?.fields?.answers?.arrayValue?.values?.get(position)?.stringValue
                        ?: ""
                (mcqHolder.title.parent as RelativeLayout).background =
                    ContextCompat.getDrawable(mContext, R.drawable.gray_rectangle_new_theme)
                val gd =
                    (mcqHolder.title.parent as RelativeLayout).background as GradientDrawable
                if ((surveyFields?.document?.fields?.correctAnswer?.stringValue
                        ?: "").isNotEmpty()
                ) {
                    val correctAnswer = surveyFields?.document?.fields?.correctAnswer?.stringValue
                        ?: ""
                    val selectedAnswer =
                        this.surveyFields?.document?.fields?.answers?.arrayValue?.values?.get(
                            position
                        )?.stringValue ?: ""

                    if ((this.surveyFields?.selectedOptions ?: arrayListOf()).isNotEmpty()) {
                        if (correctAnswer == (this.surveyFields?.selectedOptions
                                ?: arrayListOf())[0] && correctAnswer == selectedAnswer || correctAnswer == selectedAnswer
                        ) {
                            gd.setColor(Color.parseColor(BabbleSDKController.getInstance(mContext)!!.greenColor))
                            mcqHolder.title.setTextColor(
                                ContextCompat.getColor(mContext, R.color.txtblack)
                            )
                        } else if ((this.surveyFields?.selectedOptions
                                ?: arrayListOf())[0] == selectedAnswer
                        ) {
                            gd.setColor(Color.parseColor(BabbleSDKController.getInstance(mContext)!!.redColor))
                            mcqHolder.title.setTextColor(
                                ContextCompat.getColor(mContext, R.color.white)
                            )
                        } else {
                            gd.setColor(
                                ContextCompat.getColor(
                                    mContext,
                                    R.color.new_theme_gray
                                )
                            ) //R.color.new_theme_gray));
                            mcqHolder.title.setTextColor(
                                ContextCompat.getColor(mContext, R.color.txtblack)
                            )
                        }
                    } else {
                        gd.setColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.new_theme_gray
                            )
                        ) //R.color.new_theme_gray));
                        mcqHolder.title.setTextColor(
                            ContextCompat.getColor(mContext, R.color.txtblack)
                        )
                    }
                } else {
                    if (this.surveyFields?.selectedOptions?.contains(
                            this.surveyFields?.document?.fields?.answers?.arrayValue?.values?.get(
                                position
                            )?.stringValue ?: ""
                        ) == true
                    ) {
                        try {
                            gd.setColor(Color.parseColor(BabbleSDKController.getInstance(mContext)!!.themeColor))
                        } catch (nfe: NumberFormatException) {
                            BabbleSDKController.getInstance(mContext)!!.themeColor =
                                "#" + Integer.toHexString(
                                    ContextCompat.getColor(
                                        mContext,
                                        R.color.colorPrimaryDark
                                    )
                                )
                            gd.setColor(Color.parseColor(BabbleSDKController.getInstance(mContext)!!.themeColor))
                        }
                        mcqHolder.title.setTextColor(
                            ContextCompat.getColor(mContext, R.color.white)
                        )
                    } else {
                        gd.setColor(
                            ContextCompat.getColor(
                                mContext,
                                R.color.new_theme_gray
                            )
                        ) //R.color.new_theme_gray));
                        mcqHolder.title.setTextColor(
                            ContextCompat.getColor(mContext, R.color.txtblack)
                        )
                    }
                }
                (mcqHolder.title.parent as RelativeLayout).setOnClickListener {
                    babbleClickHandler.itemClicked(position)
                }
            }

            1 -> {
                val ratingHolder: RatingsViewHolder = holder as RatingsViewHolder
                ratingHolder.title.text = "${position + 1}"
                (ratingHolder.title.parent as RelativeLayout).background =
                    ContextCompat.getDrawable(mContext, R.drawable.gray_rectangle_new_theme)
                val gd =
                    (ratingHolder.title.parent as RelativeLayout).background as GradientDrawable
                if (this.surveyFields?.selectedRating == (position + 1)) {
                    try {
                        gd.setColor(Color.parseColor(BabbleSDKController.getInstance(mContext)!!.themeColor))
                    } catch (nfe: NumberFormatException) {
                        BabbleSDKController.getInstance(mContext)!!.themeColor =
                            "#" + Integer.toHexString(
                                ContextCompat.getColor(
                                    mContext,
                                    R.color.colorPrimaryDark
                                )
                            )
                        gd.setColor(Color.parseColor(BabbleSDKController.getInstance(mContext)!!.themeColor))
                    }
                    ratingHolder.title.setTextColor(
                        ContextCompat.getColor(mContext, R.color.white)
                    )
                } else {
                    gd.setColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.new_theme_gray
                        )
                    ) //R.color.new_theme_gray));
                    ratingHolder.title.setTextColor(
                        ContextCompat.getColor(mContext, R.color.txtblack)
                    )
                }
                (ratingHolder.title.parent as RelativeLayout).setOnClickListener {
                    babbleClickHandler.itemClicked(position)
                }
            }

            2 -> {
                val ratingStarViewHolder: RatingsStarViewHolder = holder as RatingsStarViewHolder
                if ((position + 1) <= (this.surveyFields?.selectedRating ?: -1)) {
                    ratingStarViewHolder.stars.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.selected_star)
                    )
                } else {
                    ratingStarViewHolder.stars.setImageDrawable(
                        ContextCompat.getDrawable(mContext, R.drawable.unselected_star)
                    )
                }
                (ratingStarViewHolder.stars.parent as RelativeLayout).setOnClickListener {
                    babbleClickHandler.itemClicked(position)
                }
            }

            3 -> {
                val ratingsEmojiViewHolder: RatingsEmojiViewHolder =
                    holder as RatingsEmojiViewHolder
                ratingsEmojiViewHolder.emoji.text = mContext.resources.getString(
                    emojis[position]
                )
                (ratingsEmojiViewHolder.emoji.parent
                    .parent as BabbleDynamicSquare).setBackgroundResource(R.drawable.rounded_rectangle_unselected)
                val gdEmojis =
                    (ratingsEmojiViewHolder.emoji.parent
                        .parent as BabbleDynamicSquare).background as GradientDrawable
                if (this.surveyFields?.selectedRating == (position + 1)) {
                    gdEmojis.setColor(Color.parseColor(BabbleSDKController.getInstance(mContext)!!.themeColor))
                } else {
                    gdEmojis.setColor(
                        BabbleSdkHelper.manipulateColor(
                            Color.parseColor("#000000"),
                            0.0f
                        )
                    )
                }

                (ratingsEmojiViewHolder.emoji.parent as RelativeLayout).setOnClickListener {
                    babbleClickHandler.itemClicked(position)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return listSize
    }

    init {
        mInflater = LayoutInflater.from(mContext)
        this.mContext = mContext
        this.babbleClickHandler = babbleClickHandler
        this.surveyFields = surveyFields
        when (this.surveyFields?.document?.fields?.questionTypeId?.integerValue ?: "9") {
            "1", "2" -> {
                viewType = 0
                listSize =
                    this.surveyFields?.document?.fields?.answers?.arrayValue?.values?.size ?: 0
            }

            "4" -> {
                viewType = 1
                listSize = 10
            }

            "5" -> {
                viewType = 1
                listSize = 5
            }

            "7" -> {
                viewType = 2
                listSize = 5
            }

            "8" -> {
                viewType = 3
                listSize = 5
            }

            else -> {
                viewType = -1
                listSize = 0
            }
        }
    }

    fun notifyMyList(surveyInputs: UserQuestionResponse) {
        this.surveyFields = null
        this.surveyFields = surveyInputs
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return viewType
    }
}
/*
 * * Copyright(c)2021.蛮羊 Inc.All rights reserved.
 */
package com.apemans.custom.adapter

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.apemans.custom.CustomFaqHomeActivity
import com.apemans.custom.R
import com.apemans.custom.adapter.FeedbackAdapter.FeedBackHolder
import com.apemans.custom.album.MediaConstant
import com.apemans.custom.bean.ContentType
import com.apemans.custom.bean.RecordDataBean
import com.apemans.custom.bean.Status
import com.apemans.custom.bean.UserType
import com.apemans.custom.review.ReviewActivity
import com.apemans.base.utils.DisplayHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.dylanc.longan.startActivity
import java.text.SimpleDateFormat
import java.util.*

/**
 * @Auther: 蛮羊
 * @datetime: 2021/10/16
 * @desc:反馈内容适配器，样式控制
 */
class FeedbackAdapter : RecyclerView.Adapter<FeedBackHolder>() {
    private val dataList: MutableList<RecordDataBean> = ArrayList()

    private var mOnClickListener: OnItemStatusClickListener? = null
    //精确到毫秒，是因为在重新发送数据的时候，有可能1s内发送多条，这样时间顺序排序会失去意义（最后会按id排序）
    @SuppressLint("SimpleDateFormat")
    private val formatter: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val formatterShow: SimpleDateFormat = SimpleDateFormat("hh:mm a")

    private   var userPhoto = ""
    private var dp10 = DisplayHelper.dpToPx(10)
    private val multiTransformation = MultiTransformation(
        CenterCrop(), RoundedCorners(
            dp10
        )
    )

    /**
     * 记录当前展示时间，后续时间如果不超过一分钟则不显示
     */
    private var tvDateShow = "";

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedBackHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.customservice_activity_feedback, parent, false)
        return FeedBackHolder(view)
    }

    override fun onBindViewHolder(holder: FeedBackHolder, position: Int) {
        val item = dataList.get(position)
        if (item.userType == UserType.ROBOT) {  //问题热点
            holder.clRobot.visibility = View.VISIBLE
            holder.clCustom.visibility = View.GONE
            holder.clNormalQuestion.visibility = View.VISIBLE
            holder.tvRobotContent.visibility = View.GONE
        } else if (item.userType == UserType.SERVICE) { //服务机器人()
            holder.clNormalQuestion.visibility = View.GONE
            holder.tvRobotContent.visibility = View.VISIBLE
            holder.tvRobotContent.text = item.content
        } else if (item.userType == UserType.CUSTOMER) {    //用户界面
            holder.clRobot.visibility = View.GONE
            holder.clCustom.visibility = View.VISIBLE
            if (!TextUtils.isEmpty(userPhoto)) {
                Glide.with(holder.ivCustomAvatar).load(userPhoto).transform(multiTransformation)
                    .into(holder.ivCustomAvatar)
            }

            if (item.contentType == ContentType.IMAGE || item.contentType == ContentType.VIDEO) {
                holder.ivPicture.visibility = View.VISIBLE
                holder.tvContent.visibility = View.GONE

                Glide.with(holder.ivPicture).load(item.content).transform(multiTransformation)
                    .into(holder.ivPicture)
                //点击图片进入预览页
                holder.ivPicture.setOnClickListener {
                    startActivity<ReviewActivity>(
                        Pair(
                            MediaConstant.MEDIA_TYPE,
                            item.contentType
                        ), Pair(MediaConstant.MEDIA_PATH, item.content)
                    )
                }
                when (item.status) {
                    //对发送失败的消息做处理
                    Status.NETWORK_EXCEPTION -> {
                        holder.status.visibility = View.VISIBLE
                    }
                    Status.SUCCESS -> {
                        holder.status.visibility = View.GONE
                    }

                }
            } else if (item.contentType == ContentType.TXT) {
                holder.tvContent.text = item.content
                holder.tvContent.visibility = View.VISIBLE
                holder.ivPicture.visibility = View.GONE
            }


        } else if (item.userType == UserType.SYSTEM) {  //图片布局
            holder.tvContent.visibility = View.GONE
            holder.ivPicture.visibility = View.VISIBLE

        }
        if (item.contentType == ContentType.VIDEO) { //视频图标
            holder.ivVideoIco.visibility = View.VISIBLE
        } else {
            holder.ivVideoIco.visibility = View.GONE
        }

        //日期,避免同时
//        if (item.date == tvDateShow) {
//            holder.tvDate.visibility = View.GONE
//        } else {
//            tvDateShow = item.date
//            holder.tvDate.text = item.date
//            holder.tvDate.visibility = View.VISIBLE
//        }
        holder.tvDate.text = item.date
        holder.tvDate.visibility = View.VISIBLE


        holder.llNormalQuestion.setOnClickListener {
            startActivity<CustomFaqHomeActivity>()
        }
        holder.tvEmail.setOnClickListener {
            try {
                openEmail(it.context, "", "", "support@apemans.com")
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(
                    it.context,
                    it.context.getString(R.string.help_faq_email_us),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (mOnClickListener!=null){
            holder.status.setOnClickListener {
                Log.e("--status-","setOnClickListener")
                mOnClickListener!!.statusOnClick(item,position) }
        }


    }



    /**
     * 邮件分享
     *
     * @param context 上下文
     * @param title   邮件主题
     * @param content 邮件内容
     * @param address 邮件地址 客服邮箱
     */
    private fun openEmail(mContext: Context, title: String, content: String, address: String) {
        val uri = Uri.parse("mailto:$address")
        val emailIntent = Intent(Intent.ACTION_SENDTO, uri)
        // 设置对方邮件地址
        emailIntent.putExtra(Intent.EXTRA_EMAIL, address)
        // 设置标题内容
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, title)
        // 设置邮件文本内容
        emailIntent.putExtra(Intent.EXTRA_TEXT, content)
        mContext.startActivity(emailIntent)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setDataList(list: List<RecordDataBean>?) {
        if (list != null) {
            dataList.clear()
            dataList.addAll(list)
            notifyDataSetChanged()
        }
    }



    fun  setUserPhoto(userPhotoNow:String){
        userPhoto = userPhotoNow
    }

    class FeedBackHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val clRobot: View = itemView.findViewById(R.id.clRobot)
        val tvRobotContent: TextView = itemView.findViewById(R.id.tvRobotContent)
        val clNormalQuestion: View = itemView.findViewById(R.id.clNormalQuestion)
        val llNormalQuestion: View = itemView.findViewById(R.id.llNormalQuestion)
        val clCustom: View = itemView.findViewById(R.id.clCustom)
        val ivCustomAvatar: ImageView = itemView.findViewById(R.id.ivCustomAvatar)
        val tvContent: TextView = itemView.findViewById(R.id.tvContent)
        val ivPicture: ImageView = itemView.findViewById(R.id.ivPicture)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val ivVideoIco: ImageView = itemView.findViewById(R.id.ivPicture_video)
        val status: ImageView = itemView.findViewById(R.id.iv_status)
    }

    fun setOnClickListener(onClickListener: OnItemStatusClickListener) {
        mOnClickListener = onClickListener
    }
    /**
     * 回调状态点击事件
     */
    interface OnItemStatusClickListener {
        fun statusOnClick(item: RecordDataBean,position:Int)

    }
}
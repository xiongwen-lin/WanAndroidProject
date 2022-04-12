package com.apemans.usercomponent.mine.bean

class NooieFeedbackOption {
    private var feedbackTypes: List<FeedbackType?>? = null
    private var feedbackProducts: List<FeedbackProduct?>? = null

    fun getFeedbackTypes(): List<FeedbackType?>? {
        return feedbackTypes
    }

    fun setFeedbackTypes(feedbackTypes: List<FeedbackType?>?) {
        this.feedbackTypes = feedbackTypes
    }

    fun getFeedbackProducts(): List<FeedbackProduct?>? {
        return feedbackProducts
    }

    fun setFeedbackProducts(feedbackProducts: List<FeedbackProduct?>?) {
        this.feedbackProducts = feedbackProducts
    }

}
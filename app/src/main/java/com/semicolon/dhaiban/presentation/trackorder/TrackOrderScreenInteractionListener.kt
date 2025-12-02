package com.semicolon.dhaiban.presentation.trackorder

import com.semicolon.dhaiban.presentation.trackorder.composables.RefundError

interface TrackOrderScreenInteractionListener {
    fun onReviewTextChange (text:String)
    fun onRateChange (rate:Float)
    fun onClickUpButton()
    fun onClickReturnProduct(orderProductId: Int)
    fun onClickReview(orderProductId: Int)
    fun onDismissRefundItemsBottomSheet()
    fun onDismissReviewDailog()
    fun onSelectRefundReason(optionId: Int)
    fun onClickNext()
    fun onDismissReturnProductBottomSheet()
    fun onClickSend(selectedReasonId: Int, userComment: String)
    fun onRefundError(refundError: RefundError)
    fun onDismissBottomSheet()
    fun onClickNotification()
    fun onClickChat(item:Int)
}
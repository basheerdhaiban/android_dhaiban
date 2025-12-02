package com.semicolon.domain.entity


//data class PayTabsInitRequest(
//    val order_id: Int
//)
//
//data class PayTabsReturnRequest(
//    val tran_ref: Int
//)
//
//data class PayTabsStatusRequest(
//    val tran_ref: Int
//)
//
//data class PayTabsCallbackRequest(
//    val tran_ref: Int,
//    val cart_id: Int,
//)
//data class PayTabsInitModel(
//    @SerializedName("cart_id")
//    val cartId: String? = null,
//    @SerializedName("redirect_url")
//    val redirectUrl: String? = null
//)
/**
{"redirect_url":"https:\/\/secure-egypt.paytabs.com\/payment\/page\/53E6317A82E42AFF935D74E1738F2CFFEC033159C7E0766F66DC015C","cart_id":"636_785_1763046499"}
 */
data class PayTabsInitModel(
    val cart_id: String? = null,
    val redirect_url: String? = null
)
data class PayTabsModel(
    val cart_id: String? = null,
    val redirect_url: String? = null,
    val order_id: String? = null,
    val amount: String? = null,
    val currency: String? = null,
    val status: String? = null,
    val tran_ref: String? = null,
    val paid_at: String? = null
)


//{
//    "order_id": 166
//}
//{
//    "redirect_url": "https://secure-egypt.paytabs.com/payment/wr/521DD5EE82E75766938F6232B389EDCF88C0670301F7587D655BBD4…,
//    "cart_id": "166_833_1760432374"
//}
//
//{
//    "status": "----",
//    "tran_ref": "----"
//}

//{
//    "status":  "string",
//    "tran_ref": 86567,
//    "cart_id": 23,
//    "order_id":  45,
//    "amount": 3,
//    "currency": us,
//    "paid_at": "string",
//}
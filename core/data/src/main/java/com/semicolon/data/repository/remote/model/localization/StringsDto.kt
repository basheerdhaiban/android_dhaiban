package com.semicolon.data.repository.remote.model.localization


import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class StringsDto(
    @SerializedName("Accepted")
    val accepted: String?,
    @SerializedName("Account")
    val account: String?,
    @SerializedName("account_verification")
    val accountVerification: String?,
    @SerializedName("+Add Address")
    val addAddressWithPlusPrefix: String?,
    @SerializedName("+Add_Address")
    val addAddress: String?,
    @SerializedName("+Add more")
    val addMore: String?,
    @SerializedName("+ Add_new_Credit / Debit_Card")
    val addNewCreditDebitCard: String?,
    @SerializedName("Add_to_card")
    val addToCard: String?,
    @SerializedName("Address")
    val address: String?,
    @SerializedName("Address_name")
    val addressName: String?,
    @SerializedName("Address_Type")
    val addressType: String?,
    @SerializedName("already_have_an_account")
    val alreadyHaveAnAccount: String?,
    @SerializedName("app_name")
    val appName: String?,
    @SerializedName("Apply")
    val apply: String?,
    @SerializedName("Are_You_sure_you_want_to_delete_account?")
    val areYouSureYouWantToDeleteAccount: String?,
    @SerializedName("Are_You_sure_yo_want_to_log_out?")
    val areYouSureYouWantToLogOut: String?,
    @SerializedName("Available_Coupons")
    val availableCoupons: String?,
    @SerializedName("Back")
    val back: String?,
    @SerializedName("Best_seller")
    val bestSeller: String?,
    @SerializedName("blank_email")
    val blankEmail: String?,
    @SerializedName("blank_otp")
    val blankOtp: String?,
    @SerializedName("blank_password")
    val blankPassword: String?,
    @SerializedName("blank_phone")
    val blankPhone: String?,
    @SerializedName("blank_username")
    val blankUsername: String?,
    @SerializedName("Can I Track my order's delivery status?")
    val canITrackMyOrdersDeliveryStatus: String?,
//    @SerializedName("cart")
//    val cartInSmallCase: String?,
    @SerializedName("cart")
    val cart: String?,
//    @SerializedName("Cash")
    val Cash: String?,
//    @SerializedName("categories")
//    val categoriesInSmallCase: String?,
    @SerializedName("categories")
    val categories: String?,
    @SerializedName("Category")
    val category: String?,
    @SerializedName("Change")
    val change: String?,
    @SerializedName("City")
    val city: String?,
    @SerializedName("Clear")
    val clear: String?,
    @SerializedName("Color")
    val color: String?,
    @SerializedName("confirm")
    val confirm: String?,
//    @SerializedName("confirm")
//    val confirmInSmallCase: String?,
    @SerializedName("confirm_Location")
    val confirmLocation: String?,
    @SerializedName("Contact_Number")
    val contactNumber: String?,
    @SerializedName("Contact_Us")
    val contactUs: String?,
    @SerializedName("Continue_for_payment")
    val continueForPayment: String?,
    @SerializedName("country")
    val country: String?,
    @SerializedName("Coupons")
    val coupons: String?,
    @SerializedName("Credit / Debit cards")
    val creditDebitCards: String?,
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("Current_Orders")
    val currentOrders: String?,
    @SerializedName("Current_Password")
    val currentPassword: String?,
    @SerializedName("Current_Requests")
    val currentRequests: String?,
    @SerializedName("Customer_Service")
    val customerService: String?,
    @SerializedName("Declined")
    val declined: String?,
    @SerializedName("Default")
    val default: String?,
    @SerializedName("Delete_account")
    val deleteAccount: String?,
    @SerializedName("Delivered")
    val delivered: String?,
    @SerializedName("Delivery_Address")
    val deliveryAddress: String?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Detailed_Address")
    val detailedAddress: String?,
    @SerializedName("don_t_have_an_account")
    val donTHaveAnAccount: String?,
    @SerializedName("Done")
    val done: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("email_hint")
    val emailHint: String?,
    @SerializedName("enter_phone_number_to_reset_password")
    val enterPhoneNumberToResetPassword: String?,
    @SerializedName("FAQ")
    val fAQ: String?,
    @SerializedName("Facebook")
    val facebook: String?,
    @SerializedName("Fashion")
    val fashion: String?,
    @SerializedName("favourites")
    val favourites: String?,
    @SerializedName("Filter")
    val filter: String?,
    @SerializedName("Flash_sale")
    val flashSale: String?,
    @SerializedName("forget_password_app_bar_title")
    val forgetPasswordAppBarTitle: String?,
    @SerializedName("Government")
    val government: String?,
    @SerializedName("Hi")
    val hi: String?,
//    @SerializedName("home")
//    val homeInSmallCase: String?,
    @SerializedName("home")
    val home: String?,
    @SerializedName("Instagram")
    val instagram: String?,
    @SerializedName("invalid_email")
    val invalidEmail: String?,
    @SerializedName("invalid_otp")
    val invalidOtp: String?,
    @SerializedName("invalid_phone")
    val invalidPhone: String?,
    @SerializedName("Items_in_your_cart")
    val itemsInYourCart: String?,
    @SerializedName("language")
    val language: String?,
    @SerializedName("LogIn")
    val logIn: String?,
    @SerializedName("log_out")
    val logOut: String?,
    @SerializedName("login_by")
    val loginBy: String?,
    @SerializedName("login_screen_header")
    val loginScreenHeader: String?,
    @SerializedName("Lorem Ipsum is simply dummy text of the printing and typesetting industry.")
    val testSmallDescription: String?,
    @SerializedName("Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.")
    val testDescription: String?,
    @SerializedName("Material")
    val material: String?,
    @SerializedName("mobile_number")
    val mobileNumber: String?,
    @SerializedName("my_profile")
    val myProfile: String?,
    @SerializedName("New_Password")
    val newPassword: String?,
    @SerializedName("new_password_hint")
    val newPasswordHint: String?,
    @SerializedName("new_password_label")
    val newPasswordLabel: String?,
    @SerializedName("New_Product")
    val newProduct: String?,
    @SerializedName("next")
    val next: String?,
//    @SerializedName("next")
//    val nextInSmallCase: String?,
    @SerializedName("not_similar_passwords")
    val notSimilarPasswords: String?,
    @SerializedName("Order_Date:")
    val orderDate: String?,
    @SerializedName("Order_ID:")
    val orderID: String?,
    @SerializedName("Order_Number:")
    val orderNumber: String?,
    @SerializedName("Orders")
    val orders: String?,
    @SerializedName("otp_email_description")
    val otpEmailDescription: String?,
    @SerializedName("otp_phone_description")
    val otpPhoneDescription: String?,
    @SerializedName("password")
    val password: String?,
    @SerializedName("Payment")
    val payment: String?,
    @SerializedName("Payment_Details")
    val paymentDetails: String?,
    @SerializedName("Payment_Method:")
    val paymentMethod: String?,
    @SerializedName("Payment_Status:")
    val paymentStatus: String?,
    @SerializedName("Pending")
    val pending: String?,
    @SerializedName("phone_number_hint")
    val phoneNumberHint: String?,
    @SerializedName("Place order")
    val placeOrder: String?,
    @SerializedName("Postal_Code")
    val postalCode: String?,
    @SerializedName("Previous_Orders")
    val previousOrders: String?,
    @SerializedName("Previous_Requests")
    val previousRequests: String?,
    @SerializedName("Price")
    val price: String?,
    @SerializedName("Price_Range")
    val priceRange: String?,
    @SerializedName("Privacy_Policy")
    val privacyPolicy: String?,
    @SerializedName("Product_details")
    val productDetails: String?,
    @SerializedName("profile")
    val profile: String?,
    @SerializedName("Promo_code")
    val promoCode: String?,
    @SerializedName("Properties")
    val properties: String?,
    @SerializedName("Rate_us")
    val rateUs: String?,
    @SerializedName("Re-enter_new_Password")
    val reEnterNewPassword: String?,
    @SerializedName("re_enter_new_password_label")
    val reEnterNewPasswordLabel: String?,
    @SerializedName("Re-order")
    val reOrder: String?,
    @SerializedName("Ready_for_shipping")
    val readyForShipping: String?,
    @SerializedName("Receiver:")
    val `receiver`: String?,
    @SerializedName("refund")
    val refund: String?,
    @SerializedName("Refund_items")
    val refundItems: String?,
    @SerializedName("resend_code")
    val resendCode: String?,
//    @SerializedName("reset")
//    val resetInSmallCase: String?,
    @SerializedName("reset")
    val reset: String?,
    @SerializedName("reset_password")
    val resetPassword: String?,
    @SerializedName("Return_Product")
    val returnProduct: String?,
    @SerializedName("Reviews")
    val reviews: String?,
    @SerializedName("(ReviewDto)")
    val reviewsWithBraces: String?,
    @SerializedName("Save")
    val save: String?,
    @SerializedName("search")
    val search: String?,
    @SerializedName("Send")
    val send: String?,
    @SerializedName("Set_as_default_Address")
    val setAsDefaultAddress: String?,
    @SerializedName("Shipping_Policy")
    val shippingPolicy: String?,
    @SerializedName("short_otp")
    val shortOtp: String?,
    @SerializedName("short_password")
    val shortPassword: String?,
    @SerializedName("short_username")
    val shortUsername: String?,
    @SerializedName("Show_details")
    val showDetails: String?,
    @SerializedName("sign_up")
    val signup: String?,
    @SerializedName("Size")
    val size: String?,
    @SerializedName("skip")
    val skip: String?,
//    @SerializedName("skip")
//    val skipInSmallCase: String?,
    @SerializedName("Sort_by")
    val sortBy: String?,
    @SerializedName("success")
    val success: String?,
    @SerializedName("Terms & Conditions")
    val termsConditions: String?,
    @SerializedName("Today's_Offers")
    val todaysOffers: String?,
    @SerializedName("Top_brands")
    val topBrands: String?,
    @SerializedName("Total")
    val total: String?,
    @SerializedName("Track_Order")
    val trackOrder: String?,
    @SerializedName("Twitter")
    val twitter: String?,
    @SerializedName("Types_of_data_we_collect")
    val typesOfDataWeCollect: String?,
    @SerializedName("Under_Review")
    val underReview: String?,
    @SerializedName("username")
    val username: String?,
    @SerializedName("username_hint")
    val usernameHint: String?,
    @SerializedName("View_all")
    val viewAll: String?,
    @SerializedName("Wallet")
    val wallet: String?,
    @SerializedName("Website")
    val website: String?,
    @SerializedName("Welcome")
    val welcome: String?,
    @SerializedName("Whatsapp")
    val whatsapp: String?,
    @SerializedName("Work")
    val work: String?,
    @SerializedName("Write_your_comment")
    val writeYourComment: String?,
    @SerializedName("Your_balance")
    val yourBalance: String?,
    @SerializedName("Qty_to_Refund:")
    val qtyToRefund: String?,
    @SerializedName("Recommended_for_ you")
    val recommendedForYou: String?,
    @SerializedName("Out_of_stock")
    val outOfStock: String?,
    @SerializedName("address_required")
    val addressRequired: String?,
    @SerializedName("detailed_address_required")
    val detailedAddressRequired: String?,
    @SerializedName("country_required")
    val countryRequired: String?,
    @SerializedName("government_required")
    val governmentRequired: String?,
    @SerializedName("city_required")
    val cityRequired: String?,
    @SerializedName("region_required")
    val regionRequired: String?,
    @SerializedName("postal_code_required")
    val postalCodeRequired: String?,
    @SerializedName("contact_number_required")
    val contactNumberRequired: String?,
    @SerializedName("region")
    val region: String?,
    @SerializedName("enable_location")
    val enableLocation: String?,
    @SerializedName("location_needs_to_be_enabled")
    val locationNeedsToBeEnabled: String?,
    @SerializedName("subtotal")
    val subtotal: String?,
    @SerializedName("tax_&_other_fees")
    val taxAndOtherFees: String?,
    @SerializedName("no_data_found")
    val noDataFound: String?,
    @SerializedName("needs_login")
    val needsLogin: String?,
    @SerializedName("payment_success")
    val paymentSuccess: String?,
    @SerializedName("back_to_home")
    val backToHome: String?,
    @SerializedName("not_available")
    val notAvailable: String?,
    @SerializedName("added_successfully")
    val addedSuccessfully: String?,
    @SerializedName("something_went_wrong")
    val someThingWentWrong: String?,
    @SerializedName("choose_variant_first")
    val chooseVariantFirst: String?,
    @SerializedName("edit_cart")
    val editCart: String?,
    @SerializedName("adding_item_to_cart_requires_login")
    val addingItemToCartRequiresLogin: String?,
    @SerializedName("cancel")
    val cancel: String?,
    @SerializedName("chat_room")
    val chatRoom: String?,
    @SerializedName("try_toConnect_again")
    val tryToConnectAgain: String?,
    @SerializedName("exit")
    val exit: String?,
    @SerializedName("there_is_no_connection")
    val noConnection: String?,
    @SerializedName("noFavourite")
    val noFavourite: String?,
    @SerializedName("You_have_nothing_on_your_list_yet")
    val You_have_nothing_on_your_list_yet: String?,
    @SerializedName("its_never_too_late_to_change_it")
    val its_never_too_late_to_change_it: String?,
    @SerializedName("inbox_empty")
    val inbox_empty: String?,
    @SerializedName("You_have_no_messages_yet")
    val You_have_no_messages_yet: String?,
    @SerializedName("Be_the_first_to_start_a_conversation")
    val Be_the_first_to_start_a_conversation: String?,
    @SerializedName("chat")
    val chat: String?,
    @SerializedName("review")
    val review: String?,
    @SerializedName("tiktok")
    val tiktok: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("title_types_here")
    val title_types_here: String?,
    @SerializedName("delete")
    val delete: String?,
    @SerializedName("message_Delete_account")
    val messageDeleteAccount: String?,
    @SerializedName("youtube")
    val youTube: String?,
    @SerializedName("snapchat")
    val snapChat: String?,
    @SerializedName("weChat")
    val weChat: String?,
    @SerializedName("useYourWallet")
    val useYourWallet: String?,
    @SerializedName("partyCode")
    val partyCode: String?,
    @SerializedName("use")
    val use: String?,
    @SerializedName("momopay")
    val momoPay: String?,
    @SerializedName("available_in_your_wallet")
    val availableInYourWallet: String?,
    @SerializedName("change_Address")
    val changeAddress: String?,
    @SerializedName("exploreMe")
    val exploreMe: String?,
    @SerializedName("thankYou")
    val thankYou: String?,
    @SerializedName("Are_You_Sure?!")
    val areYouSure: String?,
    @SerializedName("stay")
    val stay: String?,
    @SerializedName("notification")
    val notification: String?,
    @SerializedName("on_Delivery")
    val onDelivery: String?,
    @SerializedName("The_order_has_been_requested_successfully")
    val The_order_has_been_requested_successfully: String?,
    @SerializedName("continue")
    val continues: String?,
    @SerializedName("check_cart")
    val checkCart: String?,
    @SerializedName("recieveds")
    val recieved: String?, //Recieved,
    @SerializedName("reach_us") // Reach Us
    val reachUs: String?,
    @SerializedName("product")
    val product: String?,
    @SerializedName("totalPrice")
    val totalPrice: String? //Total Price:
  ,  @SerializedName("YourCartIsEmpty") // Reach Us
    val YourCartIsEmpty: String ?,
    @SerializedName("RefundReasons")
    val RefundReasons: String? ,
    @SerializedName("Products")
    val Products: String?,
    @SerializedName("quantity")
    val Quantity: String?,
    @SerializedName("Current_Refunds")
    val currentRefunds: String,
    @SerializedName("Previous_Refunds")
    val previousRefunds: String,
    @SerializedName("approval")
    val approval: String ,
    @SerializedName("amount")

    val amount: String ,
    @SerializedName("date")
    val date: String,
    @SerializedName("there_is_no_product")
    val thereIsnoProduct: String,

)
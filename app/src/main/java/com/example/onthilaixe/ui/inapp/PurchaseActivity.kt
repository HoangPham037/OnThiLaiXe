package com.example.onthilaixe.ui.inapp;

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ConsumeParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.example.onthilaixe.R
import com.google.common.collect.ImmutableList


class PurchaseActivity : AppCompatActivity(), PurchaseAdapter.OnClickListener {
    private var adapterc: PurchaseAdapter? = null
    private var billingClientc: BillingClient? = null
    private var handlerc: Handler? = null
    private var productDetailsListc: MutableList<ProductDetails>? = null
    private var onPurchaseResponsec: OnPurchaseResponse? = null
    private var listDatac: RecyclerView? = null
    private var imgBackc: ImageView? = null
    private var LllNoData: LinearLayout? = null

    private lateinit var pref: PrefHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)
        pref = PrefHelper.getInstance(this)!!
        initViews()
        imgBackc!!.setOnClickListener { onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
        billingClientc!!.queryPurchasesAsync(
            QueryPurchasesParams.newBuilder().setProductType(BillingClient.ProductType.INAPP)
                .build()
        ) { billingResult: BillingResult, list: List<Purchase> ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                for (purchase in list) {
                    if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
                        verifyInAppPurchase(purchase)
                    }
                }
            }
        }
    }

    private fun initViews() {
        listDatac = findViewById(R.id.listData)
        imgBackc = findViewById(R.id.imvBack)
        adapterc = PurchaseAdapter()
        LllNoData = findViewById(R.id.LllNoData)
        listDatac?.setHasFixedSize(true)
        listDatac?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        listDatac?.adapter = adapterc
        adapterc!!.setOnClickListener(this)
        productDetailsListc = ArrayList()
        handlerc = Handler()
        billingClientc = BillingClient.newBuilder(this).enablePendingPurchases()
            .setListener { _: BillingResult?, _: List<Purchase?>? -> }
            .build()
        establishConnection()
    }

    fun establishConnection() {
        billingClientc!!.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    showProducts()
                }
            }

            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                establishConnection()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun showProducts() {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(inAppProductList)
            .build()
        billingClientc!!.queryProductDetailsAsync(
            params
        ) { billingResult: BillingResult?, prodDetailsList: List<ProductDetails> ->
            // Process the result
            productDetailsListc!!.clear()
            handlerc!!.postDelayed({
                productDetailsListc!!.addAll(prodDetailsList)
                adapterc!!.setData(this, productDetailsListc)
                if (prodDetailsList.isEmpty()) {
                    LllNoData?.visibility = View.VISIBLE
                    listDatac?.visibility = View.GONE
                    Toast.makeText(
                        this@PurchaseActivity,
                        "prodDetailsList, size = 0",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    LllNoData?.visibility = View.GONE
                    listDatac?.visibility = View.VISIBLE
                }
            }, 2000)
        }
    }

    //Product 1
    //Product 2
    //Product 3
    //Product 4
    //Product 5
    //Product 6
    private val inAppProductList: ImmutableList<QueryProductDetailsParams.Product>
        private get() = ImmutableList.of(
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constant.KEY_GOLD_1)
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),//Product 1
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constant.KEY_GOLD_2)
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),    //Product 2
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constant.KEY_GOLD_3)
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),  //Product 3
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constant.KEY_GOLD_4)
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),  //Product 4
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constant.KEY_GOLD_5)
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),  //Product 5
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constant.KEY_GOLD_6)
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),  //Product 6
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constant.KEY_GOLD_7)
                .setProductType(BillingClient.ProductType.INAPP)
                .build(),
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(Constant.KEY_GOLD_8)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        )

    fun verifyInAppPurchase(purchases: Purchase) {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams
            .newBuilder()
            .setPurchaseToken(purchases.purchaseToken)
            .build()
        billingClientc!!.acknowledgePurchase(acknowledgePurchaseParams) { billingResult: BillingResult ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                val proId = purchases.products[0]
                val quantity = purchases.quantity
                setPurchaseResponse(object : OnPurchaseResponse {
                    override fun onResponse(proId: String?, quantity: Int) {
                        proId?.let {
                            setupResult(
                                it,
                                quantity
                            )
                        }
                    }
                })
                onPurchaseResponsec!!.onResponse(proId, quantity)
                allowMultiplePurchases(purchases)
            }
        }
    }

    private fun allowMultiplePurchases(purchase: Purchase) {
        val consumeParams = ConsumeParams
            .newBuilder()
            .setPurchaseToken(purchase.purchaseToken)
            .build()
        billingClientc!!.consumeAsync(consumeParams) { _, _ ->
            Toast.makeText(
                this@PurchaseActivity,
                " Resume item ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClickItem(item: ProductDetails) {
        launchPurchaseFlow(item)
    }

    private fun launchPurchaseFlow(productDetails: ProductDetails) {
        val productDetailsParamsList = ImmutableList.of(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(productDetails)
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParamsList)
            .build()
        billingClientc!!.launchBillingFlow(this, billingFlowParams)
    }

    private fun setupResult(proId: String, quantity: Int) {
        val intent = Intent()
        val totalCoin = pref.getValueCoin()
        val remainCoin = totalCoin ?: 0 + getCoinFromKey(proId) * quantity;
        pref.setValueCoin(remainCoin);
        setResult(RESULT_OK, intent)
        runOnUiThread { onBackPressed() }
    }

    private fun getCoinFromKey(coinId: String): Int {
        return when (coinId) {
            Constant.KEY_GOLD_1 -> 1
            Constant.KEY_GOLD_2 -> 5
            Constant.KEY_GOLD_3 -> 30
            Constant.KEY_GOLD_4 -> 50
            Constant.KEY_GOLD_5 -> 80
            Constant.KEY_GOLD_6 -> 120
            Constant.KEY_GOLD_7 -> 150
            Constant.KEY_GOLD_8 -> 250
            else -> 0
        }
    }

    internal interface OnPurchaseResponse {
        fun onResponse(proId: String?, quantity: Int)
    }

    private fun setPurchaseResponse(onPurchaseResponse: OnPurchaseResponse) {
        this.onPurchaseResponsec = onPurchaseResponse
    }
}
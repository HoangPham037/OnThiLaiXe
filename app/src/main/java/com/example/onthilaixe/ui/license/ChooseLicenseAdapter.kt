package com.example.onthilaixe.ui.license

import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import com.example.onthilaixe.R
import com.example.onthilaixe.base.recyclerview.BaseRecyclerAdapter
import com.example.onthilaixe.base.recyclerview.BaseViewHolder
import com.example.onthilaixe.databinding.ItemChooseLicenseBinding
import com.example.onthilaixe.models.local.room.entity.License

class ChooseLicenseAdapter(private val nameLicense: String?, private val event: (License) -> Unit) :
    BaseRecyclerAdapter<License, ChooseLicenseAdapter.ChooseLicenseHolder>() {
    inner class ChooseLicenseHolder(private val binding: ViewDataBinding) :
        BaseViewHolder<License>(binding) {
        override fun bind(itemData: License?) {
            super.bind(itemData)
            if (binding is ItemChooseLicenseBinding) {
                binding.apply {
                    tvLicenseType.text = itemData?.name
                    tvLicenseDesc.text = itemData?.content
                    if (nameLicense != null && itemData?.name == nameLicense) {
                        root.setBackgroundResource(R.drawable.bgr_btn_check_ques)
                        tvLicenseType.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.white
                            )
                        )
                        tvLicenseDesc.setTextColor(
                            ContextCompat.getColor(
                                itemView.context,
                                R.color.white
                            )
                        )
                    } else {
                        root.setBackgroundResource(R.drawable.bgr_item_choose_license)
                    }
                }
                onItemClickListener {
                    itemData?.let { event.invoke(it) }
                }
            }
        }
    }

    override fun getItemLayoutResource(viewType: Int): Int {
        return R.layout.item_choose_license
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseLicenseHolder {
        return ChooseLicenseHolder(getViewHolderDataBinding(parent, viewType))
    }
}
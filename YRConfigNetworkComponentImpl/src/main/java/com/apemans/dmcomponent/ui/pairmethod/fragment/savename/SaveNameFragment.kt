package com.apemans.dmcomponent.ui.pairmethod.fragment.savename

import android.view.View
import androidx.fragment.app.activityViewModels
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener
import com.apemans.dmcomponent.R
import com.apemans.dmcomponent.databinding.PairdeviceFragmentSaveNameBinding
import com.apemans.dmcomponent.ui.pairdevice.savename.items.RecommendedNameViewDelegate
import com.apemans.dmcomponent.ui.pairmethod.BasePairActivity.Companion.nextStep
import com.apemans.quickui.multitype.MultiTypeAdapter
import com.dylanc.longan.*

class SaveNameFragment : com.apemans.yruibusiness.base.BaseComponentFragment<PairdeviceFragmentSaveNameBinding>(), com.apemans.yruibusiness.utils.viewbinding.OnItemClickListener<Int> {

    private val adapter = MultiTypeAdapter(RecommendedNameViewDelegate(this))
    private val viewModel: SaveNameViewModel by activityViewModels()

    override fun onViewCreated(root: View) {

    }

    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
        adapter.items = listOf(
            R.string.add_camera_front_door,
            R.string.add_camera_office,
            R.string.add_camera_living_room,
            R.string.add_camera_garage,
            R.string.add_camera_baby_room,
            R.string.add_camera_kitchen,
        )
        binding.recyclerView.adapter = adapter
        binding.btnNext.enableWhenOtherTextNotEmpty(binding.etName)
        binding.btnNext.doOnClick {
            viewModel.saveName(binding.etName.textString)
                .observe(viewLifecycleOwner) {
                    toast("保存成功")
                    nextStep()
                }
        }
        viewModel.name.observe(viewLifecycleOwner) {
            binding.etName.setText(it)
        }
    }

    override fun onItemClick(item: Int, position: Int) {
        binding.etName.setText(item)
    }
}
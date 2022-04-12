package com.apeman.customerservice.view.album

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.apemans.yruibusiness.base.BaseComponentFragment
import com.apemans.custom.R
import com.apemans.custom.adapter.AlbumAdapter
import com.apemans.custom.bean.MediaData
import com.apemans.custom.databinding.FragmentAlbumBinding


private const val ARG_PARAM = "type"

/**
 * A simple [Fragment] subclass.
 * Use the [AlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AlbumFragment : com.apemans.yruibusiness.base.BaseComponentFragment<FragmentAlbumBinding>() {

    private var type: Int? = null
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var onMySelectMedia: OnSelectMedia
    private var mediaInfos: MutableList<MediaData> = mutableListOf()


    override fun onViewCreated(root: View) {

        onMySelectMedia = activity as OnSelectMedia
        albumAdapter = AlbumAdapter()
        if (type == 1)
            mediaInfos = mediaInfos.filter { it.duration > 0L }.toMutableList()
        else if (type == 2)
            mediaInfos = mediaInfos.filter { it.duration <= 0L }.toMutableList()

        albumAdapter.addData(mediaInfos)
        albumAdapter.setOnClickListener(object : AlbumAdapter.OnClickListener {
            override fun onItemClick(pos: Int) {
                onMySelectMedia.onMediaPath(mediaInfos[pos].path)
            }

            override fun onItemRemove(pos: Int) {
                onMySelectMedia.onRemovePath(mediaInfos[pos].path)
            }

        })
        albumAdapter.setOverCountListener(object : AlbumAdapter.OverCountListener {
            override fun overCount(count: Int) {
                Toast.makeText(context, getString(R.string.help_faq_file_max), Toast.LENGTH_SHORT)
                    .show()
            }

        })

        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.recyclerView.adapter = albumAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt(ARG_PARAM)
        }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AlbumFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(type: Int) =
            AlbumFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM, type)
                }
            }
    }


    fun addData(newMediaInfos: MutableList<MediaData>) {
        mediaInfos.clear()
        mediaInfos.addAll(newMediaInfos)
        albumAdapter.notifyDataSetChanged()
    }

    fun notifyAdapter() {
        albumAdapter.notifyDataSetChanged()
    }

    interface OnSelectMedia {
        fun onMediaPath(path: String)
        fun onRemovePath(path: String)
    }


}
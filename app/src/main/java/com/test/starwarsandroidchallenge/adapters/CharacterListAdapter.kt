package com.test.starwarsandroidchallenge.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.test.starwarsandroidchallenge.R
import com.test.starwarsandroidchallenge.dataholder.PeopleData
import com.test.starwarsandroidchallenge.util.Constants
import kotlinx.android.synthetic.main.list_type_normal.view.*

class CharacterListAdapter(private val listener: Listener) : RecyclerView.Adapter<CharacterListAdapter.BaseViewHolder<*>>() {

    interface Listener {
        fun onItemClick(peopleData : PeopleData)
    }

    private val peopleDataList: ArrayList<PeopleData> = ArrayList()
    private var peopleDataListForSearch: ArrayList<PeopleData>? = ArrayList()

    fun updateAdapter(peopleDataList: ArrayList<PeopleData>) {
        this.peopleDataList.addAll(peopleDataList)
        this.peopleDataListForSearch?.addAll(peopleDataList)
        notifyDataSetChanged()
    }

    fun restoreAdapterToInitialState() {
        this.peopleDataListForSearch?.clear()
        this.peopleDataListForSearch?.addAll(this.peopleDataList)
        notifyDataSetChanged()
    }

    fun getActualAdapterDataList(): ArrayList<PeopleData> {
        return this.peopleDataList
    }

    fun updateAdapterForSearch(peopleDataList: ArrayList<PeopleData>) {
        this.peopleDataListForSearch?.clear()
        this.peopleDataListForSearch?.addAll(peopleDataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*> {
        return when (viewType) {
            Constants.NORMAL_VIEW_TYPE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_type_normal, parent, false)
                NormalViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.list_type_empty, parent, false)
                EmptyViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (peopleDataListForSearch?.size == 0) {
            1
        } else {
            peopleDataListForSearch?.size!!
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<*>, position: Int) {
        when (holder.itemViewType) {
            Constants.NORMAL_VIEW_TYPE -> {
                if (peopleDataListForSearch?.size!! > 0) {
                    peopleDataListForSearch?.get(position)?.let { (holder as NormalViewHolder).bind(it) }
                }
            }
            else -> {
                (holder as EmptyViewHolder).bind()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (peopleDataListForSearch?.size == 0) {
            Constants.EMPTY_VIEW_TYPE
        } else {
            Constants.NORMAL_VIEW_TYPE
        }
    }

    inner class NormalViewHolder(view: View) : BaseViewHolder<PeopleData>(view) {
        override fun bind() {
            return
        }

        override fun bind(item: PeopleData) {
            itemView.setOnClickListener{ listener.onItemClick(item) }
            itemView.tv_name.text = item.name
            itemView.tv_birth_year.text = item.birth_year
        }
    }

    inner class EmptyViewHolder(view: View) : BaseViewHolder<PeopleData>(view) {
        override fun bind() {
            return
        }

        override fun bind(item: PeopleData) {
            return
        }
    }

    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: T)
        abstract fun bind()
    }
}
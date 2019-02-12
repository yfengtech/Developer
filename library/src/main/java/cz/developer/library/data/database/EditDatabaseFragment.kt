package cz.developer.library.ui.data.database

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.MenuItemCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.ShareActionProvider
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView
import cz.developer.library.R
import cz.developer.library.network.model.RequestData
import kotlinx.android.synthetic.main.fragment_edit_basebase.*
import kotlinx.android.synthetic.main.fragment_edit_basebase.view.*

/**
 * Created by cz on 2017/9/29.
 */
internal class EditDatabaseFragment: Fragment(){
    private lateinit var columnValues:Array<String>
    private lateinit var columnNames:Array<String>
    private lateinit var shareActionProvider: ShareActionProvider
    companion object {
        fun newInstance(columnNames:Array<String>,columnValues:Array<String>):Fragment{
            val fragment=EditDatabaseFragment()
            fragment.columnNames=columnNames
            fragment.columnValues=columnValues
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_edit_basebase,container,false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val layoutInflater=LayoutInflater.from(context)
        val activity=activity
        if(activity is AppCompatActivity){
            toolBar.title = getString(R.string.data_operator)
            setHasOptionsMenu(true)
            activity.setSupportActionBar(toolBar)
            activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolBar.setNavigationOnClickListener{ fragmentManager?.popBackStack() }
        }
        initSpinner()
        columnNames.zip(columnValues).forEach {
            val layout=layoutInflater.inflate(R.layout.database_edit_item,databaseLayout,false)
            val headerView=layout.findViewById(R.id.dataHeader) as TextView
            val editorView=layout.findViewById(R.id.dataEditor) as EditText
            headerView.text=it.first
            editorView.setText(it.second)
            databaseLayout.addView(layout)
        }
    }

    private fun initSpinner() {
        val adapter = ArrayAdapter(context,R.layout.spinner_dropdown_item,columnNames)
        spinner.adapter = adapter
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                setShareIntent(columnValues.getOrNull(0))
            }
            override fun onNothingSelected(adapterView: AdapterView<*>) {}
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_share_item, menu)
        val item = menu.findItem(R.id.menu_item_share)
        shareActionProvider = MenuItemCompat.getActionProvider(item) as ShareActionProvider
        shareActionProvider.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME)
        setShareIntent(columnValues[0])
    }

    private fun setShareIntent(shareData: String?) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareData)
        if (shareActionProvider != null) {
            shareActionProvider.setShareIntent(shareIntent)
        }
    }
}
package id.rackspira.seedisaster.ui.detailbencana.posko

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import id.rackspira.seedisaster.R
import id.rackspira.seedisaster.data.network.entity.DataPosko
import id.rackspira.seedisaster.data.network.entity.ListBencana
import id.rackspira.seedisaster.ui.buatposko.BuatPoskoActivity
import kotlinx.android.synthetic.main.fragment_posko.*

class PoskoFragment : Fragment(), PoskoView {

    private lateinit var poskoAdapter: PoskoAdapter
    private lateinit var presenter: PoskoPresenter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_posko, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = arguments!!.getParcelable("posisi") as ListBencana
        fabTambahPosko.setOnClickListener {
            val intent = Intent(context, BuatPoskoActivity::class.java)
            intent.putExtra("data", list)
            context!!.startActivity(intent)
        }

        presenter = PoskoPresenter(this)
        presenter.getDataPosko(list.kib!!)
        poskoAdapter = PoskoAdapter()
        recyclerviewPosko.layoutManager = LinearLayoutManager(context)
        recyclerviewPosko.adapter = poskoAdapter
    }

    override fun onSuccess(msg: String?) {}

    override fun onFailed(ms: String?) {}

    override fun onSuccessUpdate(msg: String?) {}

    override fun onFailedUpdate(msg: String?) {}

    override fun getDataPosko(dataPosko: List<DataPosko>) {
        poskoAdapter.addListPosko(dataPosko)
        if (dataPosko.isNotEmpty()) {
            imageviewPoskoKosong.visibility = View.GONE
            textviewBlmAda.visibility = View.GONE
            textviewBlmAda2.visibility = View.GONE
        } else {
            imageviewPoskoKosong.visibility = View.VISIBLE
            textviewBlmAda.visibility = View.VISIBLE
            textviewBlmAda2.visibility = View.VISIBLE
        }
    }
}

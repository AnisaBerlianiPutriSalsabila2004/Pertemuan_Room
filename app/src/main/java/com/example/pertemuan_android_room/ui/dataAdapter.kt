package com.example.pertemuan_android_room.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pertemuan_android_room.R
import com.example.pertemuan_android_room.database.Note
import com.example.pertemuan_android_room.database.NoteDao
import com.example.pertemuan_android_room.database.NoteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class dataAdapter(val dataNotes: List<Note>?): RecyclerView.Adapter<dataAdapter.MyViewHolder>(){
    lateinit var executorService: ExecutorService
    lateinit var mNotesDao: NoteDao

    class MyViewHolder (view: View):RecyclerView.ViewHolder(view){
        val tittle = view.findViewById<TextView>(R.id.title_txt)
        val desc = view.findViewById<TextView>(R.id.desc_txt)
        val date = view.findViewById<TextView>(R.id.date_txt)
        val btnUpdate = view.findViewById<Button>(R.id.btn_update)
        val btnDelete = view.findViewById<Button>(R.id.btn_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvnotes, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if(dataNotes!=null){
            return dataNotes.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(holder.itemView.context)
        mNotesDao  = db!!.noteDao()!!

        holder.tittle.text = "tittle : ${dataNotes?.get(position)?.title}"
        holder.desc.text = "description : ${dataNotes?.get(position)?.date}"
        holder.date.text = "date : ${dataNotes?.get(position)?.description}"


        holder.btnUpdate.setOnClickListener{
            val intentToDetail = Intent(holder.itemView.context, InputActivity::class.java)
            intentToDetail.putExtra("ID", dataNotes?.get(position)?.id)
            intentToDetail.putExtra("TITTLE", dataNotes?.get(position)?.title)
            intentToDetail.putExtra("DESC", dataNotes?.get(position)?.description)
            intentToDetail.putExtra("DATE", dataNotes?.get(position)?.date)
            intentToDetail.putExtra("COMMAND", "UPDATE")
            holder.itemView.context.startActivity(intentToDetail)
        }

        holder.btnDelete.setOnClickListener{
            val noteId = dataNotes?.get(position)?.id
            noteId?.let { deleteNoteById(it) }
            Toast.makeText(holder.itemView.context, "Berhasil Menghapus Data", Toast.LENGTH_SHORT).show()
            true
        }
    }

    private fun deleteNoteById(noteId: Int) {
        executorService.execute {
            mNotesDao.deleteById(noteId)
        }
    }

}
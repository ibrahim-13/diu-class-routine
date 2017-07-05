//    Copyright (C) 2017 MD. Ibrahim Khan
//
//    Project Name: 
//    Author: MD. Ibrahim Khan
//    Author's Email: ib.arshad777@gmail.com
//
//    Redistribution and use in source and binary forms, with or without modification,
//    are permitted provided that the following conditions are met:
//
//    1. Redistributions of source code must retain the above copyright notice, this
//       list of conditions and the following disclaimer.
//
//    2. Redistributions in binary form must reproduce the above copyright notice, this
//       list of conditions and the following disclaimer in the documentation and/or
//       other materials provided with the distribution.
//
//    3. Neither the name of the copyright holder nor the names of the contributors may
//       be used to endorse or promote products derived from this software without
//       specific prior written permission.
//
//    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
//    ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
//    WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
//    IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
//    INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING
//    BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
//    DATA, OR PROFITS; OR BUSINESS INTERRUPTIONS) HOWEVER CAUSED AND ON ANY THEORY OF
//    LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
//    OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
//    OF THE POSSIBILITY OF SUCH DAMAGE.

package diu.edu.bd.diuclassroutine.CardView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import diu.edu.bd.diuclassroutine.CommonData.CommonData;
import diu.edu.bd.diuclassroutine.R;

/**
 * This file was created by Arshad on 6/17/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private String[][] dataSource;

    public RecyclerAdapter(String[][] dataArgs) {
        dataSource = dataArgs;
    }

    public void setNewData(String[][] newData) { dataSource = newData; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.courseCode.setText(dataSource[position][0]);
        holder.courseName.setText(dataSource[position][1]);
        holder.room.setText(dataSource[position][2]);
        holder.time.setText(dataSource[position][3]);
        holder.courseTeacher.setText(dataSource[position][4]);
    }

    @Override
    public int getItemCount() {
        if(dataSource == null) {
            dataSource = CommonData.corruptedDatabase;
        }
        return dataSource.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        protected TextView courseCode;
        protected TextView courseName;
        protected TextView room;
        protected TextView time;
        protected TextView courseTeacher;

        public ViewHolder(View view) {
            super(view);

            courseCode = (TextView) view.findViewById(R.id.main_cardview_coursecode);
            courseName = (TextView) view.findViewById(R.id.main_cardview_coursename);
            room = (TextView) view.findViewById(R.id.main_cardview_room);
            time = (TextView) view.findViewById(R.id.main_cardview_time);
            courseTeacher = (TextView) view.findViewById(R.id.main_cardview_courseteacher);
        }
    }
}

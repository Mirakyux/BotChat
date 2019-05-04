package cn.mirakyux.botchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import cn.mirakyux.botchat.R;
import cn.mirakyux.botchat.entity.Msg;

/**
 * Created by Mi on 2017/9/11.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Msg> msgs = new ArrayList<>();
    private ViewHolder viewHolder;

    public void addMsgToAdapter(Msg m)
    {
        msgs.add(m);
    }

    public ListViewAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return msgs.size();
    }

    @Override
    public Object getItem(int position) {
        return msgs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(context)
                    .inflate(R.layout.item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Msg msg = msgs.get(position);
        if(msg.getMsg() == null || "".equals(msg.getMsg()))
        {

        }
        else if(msg.isBot())
        {
            viewHolder.computer_text.setText(msg.getMsg());
            viewHolder.computer.setVisibility(View.VISIBLE);
            viewHolder.human.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.human_text.setText(msg.getMsg());
            viewHolder.human.setVisibility(View.VISIBLE);
            viewHolder.computer.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView computer_text;
        public LinearLayout computer;
        public TextView human_text;
        public LinearLayout human;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.computer_text = (TextView) rootView.findViewById(R.id.computer_text);
            this.computer = (LinearLayout) rootView.findViewById(R.id.computer);
            this.human_text = (TextView) rootView.findViewById(R.id.human_text);
            this.human = (LinearLayout) rootView.findViewById(R.id.human);
        }

    }


}

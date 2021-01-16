package com.example.managerapp.contactsapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.managerapp.R;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends BaseAdapter implements Filterable {

    Context context;
    List<Contact> contacts;
    List<Contact> filteredContacts;

    ContactAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.filteredContacts = contacts;
    }

    @Override
    public int getCount() {
        return filteredContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    filterResults.count = contacts.size();
                    filterResults.values = contacts;
                }
                else {
                    List<Contact> resultsModel = new ArrayList<>();
                    String searchStr = constraint.toString().toLowerCase();

                    for (Contact contact : contacts){
                        if (contact.getName().toLowerCase().contains(searchStr) ||
                                contact.getSurname().toLowerCase().contains(searchStr) ||
                                contact.getEmail().toLowerCase().contains(searchStr) ||
                                contact.getPhoneNumber().toLowerCase().contains(searchStr)) {
                            resultsModel.add(contact);
                        }
                        filterResults.count = resultsModel.size();
                        filterResults.values = resultsModel;
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredContacts = (List<Contact>) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }

    private class ViewHolder {
        ImageView profile_pic;
        TextView name;
        TextView phoneNumber;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = mInflater.inflate(R.layout.list_contacts, null);
        holder = new ViewHolder();

        holder.name = (TextView) convertView
                .findViewById(R.id.contact_name);
        holder.profile_pic = (ImageView) convertView
                .findViewById(R.id.avatar);
        holder.phoneNumber = (TextView) convertView
                .findViewById(R.id.phonenumber);

        Contact contact = filteredContacts.get(position);

        if (contact.getAvatar() != null) {
            byte[] decodedString = Base64.decode(contact.getAvatar(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.profile_pic.setImageBitmap(decodedByte);
        }
        else {
            if (contact.isMale()) {
                holder.profile_pic.setImageResource(R.drawable.male);
            }
            else holder.profile_pic.setImageResource(R.drawable.female);
        }

        holder.name.setText(contact.getName() + " " + contact.getSurname());
        holder.phoneNumber.setText(contact.getPhoneNumber());

        convertView.setTag(holder);

        return convertView;
    }
}

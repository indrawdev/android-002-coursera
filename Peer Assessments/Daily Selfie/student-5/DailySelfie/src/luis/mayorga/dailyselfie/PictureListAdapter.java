package luis.mayorga.dailyselfie;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureListAdapter extends BaseAdapter{
	private static ArrayList<PictureDetails> picturesList;
	private LayoutInflater inflater;
	
	public PictureListAdapter(Context context, ArrayList<PictureDetails> results) {
		picturesList = results;
		inflater = LayoutInflater.from(context);
	}
	
	public int getCount(){
		return picturesList.size();
	}
	
	public Object getItem(int position){
		return picturesList.get(position);
	}
	
	public long getItemId(int position){
		return position;
	}
	
	static class ViewHolder {
		ImageView image;
		TextView imageName;
	}
	
	@SuppressLint("InflateParams")
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.picture_item_view, null);
			holder = new ViewHolder();
			holder.image = (ImageView) convertView.findViewById(R.id.image);
			holder.imageName = (TextView) convertView.findViewById(R.id.name);
			
			convertView.setTag(holder);
		} else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.imageName.setText(picturesList.get(position).getName());
		new ImageLoader().load(picturesList.get(position).getPath(), holder.image);
		return convertView;
	}
	
	public void add(PictureDetails newPicture) {
		picturesList.add(newPicture);
		notifyDataSetChanged();
	}
	
	public void add(ArrayList<PictureDetails> pictures) {
		for (PictureDetails pic : pictures) {
			picturesList.add(pic);
		}
		notifyDataSetChanged();
	}
	
	public void clear() {
		picturesList = new ArrayList<PictureDetails>();
		notifyDataSetChanged();
	}
}

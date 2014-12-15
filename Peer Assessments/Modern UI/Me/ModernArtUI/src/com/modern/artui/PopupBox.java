package com.modern.artui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
import android.widget.LinearLayout;

;

public class PopupBox extends Dialog {
	public PopupBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PopupBox(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public static class Builder {

		private Context context;
		private String title;
		private String message;
		private String rightButton;
		private String leftButton;
		private View contentView;

		private DialogInterface.OnClickListener rightButtonListener,
				leftButtonListener;

		public Builder(Context context) {
			this.context = context;
		}

		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}

		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}

		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}

		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}

		public Builder setRightButton(String rightButton,
				DialogInterface.OnClickListener listener) {
			this.rightButton = rightButton;
			this.rightButtonListener = listener;
			return this;
		}

		public Builder setRightButton(int rightButton,
				DialogInterface.OnClickListener listener) {
			this.rightButton = (String) context.getText(rightButton);
			this.rightButtonListener = listener;
			return this;
		}

		public Builder setLeftButton(String leftButton,
				DialogInterface.OnClickListener listener) {
			this.leftButton = leftButton;
			this.leftButtonListener = listener;
			return this;
		}

		public Builder setLeftButton(int leftButton,
				DialogInterface.OnClickListener listener) {
			this.leftButton = (String) context.getText(leftButton);
			this.leftButtonListener = listener;
			return this;
		}

		public PopupBox create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final PopupBox mPopup = new PopupBox(context, R.style.Dialog);
			View layout = inflater.inflate(R.layout.popup, null);
			mPopup.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			((TextView) layout.findViewById(R.id.title)).setText(title);
			if (rightButton != null) {
				((Button) layout.findViewById(R.id.rightButton))
						.setText(rightButton);
				if (rightButtonListener != null) {
					((Button) layout.findViewById(R.id.rightButton))
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									rightButtonListener.onClick(mPopup,
											DialogInterface.BUTTON_POSITIVE);

								}
							});
				}

			} else {
				layout.findViewById(R.id.rightButton).setVisibility(View.GONE);
			}

			if (leftButton != null) {
				((Button) layout.findViewById(R.id.leftButton))
						.setText(leftButton);
				if (leftButtonListener != null) {
					((Button) layout.findViewById(R.id.leftButton))
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									leftButtonListener.onClick(mPopup,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				layout.findViewById(R.id.leftButton).setVisibility(View.GONE);
			}

			if (message != null) {
				((TextView) layout.findViewById(R.id.message)).setText(message);
			} else if (contentView != null) {
				((LinearLayout) layout.findViewById(R.id.content))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.content)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			mPopup.setContentView(layout);
			return mPopup;

		}
	}

}

package com.indra.elections.peruvian.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class IndraTextView extends TextView {

    private static final String font = "fonts/OpenSans-Regular.ttf";
    private static final String fontBold = "fonts/OpenSans-Bold.ttf";
    private static final String fontBoldItalic = "fonts/OpenSans-BoldItalic.ttf";
    private static final String fontItalic = "fonts/OpenSans-Italic.ttf";
    private static final String fontLight = "fonts/OpenSans-Light.ttf";


	public IndraTextView(Context context) {
		super(context);
	}

	public IndraTextView(Context context, AttributeSet attrs) {
		super( context, attrs );
	}

	public IndraTextView(Context context, AttributeSet attrs, int defStyle) {
		super( context, attrs, defStyle );
	}

    @Override
	public void setTypeface(Typeface tf, int style) {

        if (!this.isInEditMode()) {
            switch (style) {
                case Typeface.NORMAL:
                    super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), font)/*, -1*/);
                    break;
                case Typeface.BOLD:
                    super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontBold)/*, -1*/);
                    break;
                case Typeface.BOLD_ITALIC:
                    super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontBoldItalic)/*, -1*/);
                    break;
                case Typeface.ITALIC:
                    super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontItalic)/*, -1*/);
                    break;
                default:
                    super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), fontLight)/*, -1*/);
                    break;
            }
        }


    }


}

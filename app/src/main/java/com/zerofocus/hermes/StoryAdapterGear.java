package com.zerofocus.hermes;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import static com.zerofocus.hermes.MainActivity.TAG;

public class StoryAdapterGear extends RecyclerView.Adapter<StoryAdapterGear.StoryViewHolder>{

    private List<Story> _stories;
    public StoryAdapterGear(List<Story> storyList) {
        _stories = storyList;
    }

    @Override
    public StoryAdapterGear.StoryViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        Log.i(TAG,"Creating new holder");
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View story_view = inflater.inflate(R.layout.story_view, parent, false);
        // Return a new holder instance
        StoryAdapterGear.StoryViewHolder storyViewHolder = new StoryAdapterGear.StoryViewHolder(story_view);
        return storyViewHolder;
    }

    @Override
    public void onBindViewHolder(StoryAdapterGear.StoryViewHolder holder, int position) {
        Log.i(TAG,"onBindViewHolder: " + position);
        final Story story = _stories.get(position);
        holder.setStory(story);
    }

    @Override
    public int getItemCount() {
        if (_stories==null)
            return 0;
        return _stories.size();
    }

    // TODO: Check if we can have a separate class for this one
    public static class StoryViewHolder extends RecyclerView.ViewHolder {

        private View _story_view;
        private Story _story;

        public TextView title_view;
        public TextView description_view;
        public TextView source_view;
        public ImageView preview_view;

        public StoryViewHolder(View item_view) {
            super(item_view);
            _story_view = item_view;

            title_view = (TextView) _story_view.findViewById(R.id.titleText_id);
            description_view = (TextView) _story_view.findViewById(R.id.descriptionText_id);
            source_view = (TextView) _story_view.findViewById(R.id.storySource_id);
            preview_view = (ImageView) _story_view.findViewById(R.id.previewImg_id);

        }

        public Story setStory(Story story){
            _story = story;
            if (story==null){
                Log.w(TAG,"Got a null story");
                return null;
            }
            Log.w(TAG,"Holding a proper story: "+story.getTitle());
            Spanned title, description;
            if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N){
                title = Html.fromHtml(story.getTitle(),Html.FROM_HTML_MODE_LEGACY);
                description = Html.fromHtml(story.getDescription(),Html.FROM_HTML_MODE_LEGACY);
            }else{
                title = Html.fromHtml(story.getTitle());
                description = Html.fromHtml(story.getDescription());
            }

            title_view.setText(title);
            description_view.setText(description);

            if (story.getImage()!=null){/*
                Glide.with(feed_view.getContext())
                        .load(story.getImage())
                        .into((ImageView) feed_view.findViewById(R.id.previewImg_id));
            */}else if (story.getImageResource()!=-1) {
                preview_view.setImageResource(story.getImageResource());
            }

            if (story.getLink() != null)
                source_view.setText(story.getLink().getHost());

            //_story_view.setOnClickListener(story);
            return story;
        }
    }

}

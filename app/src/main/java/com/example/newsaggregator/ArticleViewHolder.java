package com.example.newsaggregator;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.newsaggregator.databinding.ActivityArticlePageBinding;

public class ArticleViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView date;
    TextView author;
    ImageView newsImage;
    TextView desc;
    TextView articleCount;
    private ActivityArticlePageBinding binder;

    public ArticleViewHolder(ActivityArticlePageBinding binding) {
        super(binding.getRoot());
        this.binder = binding;

        title = binder.articleHeading;
        date = binder.articleDate;
        author = binder.articleAuthor;
        newsImage = binder.articleImage;
        desc = binder.articleDetails;
        articleCount = binder.articlePagenum;
    }
}
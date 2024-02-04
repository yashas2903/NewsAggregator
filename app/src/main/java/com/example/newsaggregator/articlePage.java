package com.example.newsaggregator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newsaggregator.databinding.ActivityArticlePageBinding;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class articlePage extends RecyclerView.Adapter<ArticleViewHolder> {

    private final MainActivity mainActivity;
    private final ArrayList<Article> articles;

    public articlePage(MainActivity activity, ArrayList<Article> list){
        this.mainActivity = activity;
        this.articles = list;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleViewHolder(ActivityArticlePageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
                //LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_article_page,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        if (article.getAuthor() != null && !article.getAuthor().isEmpty() && article.getAuthor() != "null")
        {
            holder.author.setText(article.getAuthor());
        }
        else
            holder.author.setVisibility(TextView.GONE);


// fetching the date and setting it
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try
        {
            date = parser.parse(article.getPublishedAt().split("T")[0]);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
        String formattedDate = formatter.format(date);
        holder.date.setText(formattedDate +" " + article.getPublishedAt().split("T")[1].split(":")[0] + ":" + article.getPublishedAt().split("T")[1].split(":")[1]);

// check null for date value
        if(article.getPublishedAt().equals("null") || article.getPublishedAt() == null)
        {
            holder.date.setVisibility(TextView.GONE);
        }

//setting the title
        holder.title.setText(article.getTitle());

//setting the description
        holder.desc.setText(article.getDescription() == null  || article.getDescription() == "null"
                || article.getDescription().isEmpty()? " " : article.getDescription());


// instantiate picasso
        Picasso imageDownload = Picasso.get();
        if (TextUtils.isEmpty(article.getUrlToImage()))
            holder.newsImage.setImageResource(R.drawable.noimage);
        else
            imageDownload.load(article.getUrlToImage()).error(R.drawable.brokenimage).into(holder.newsImage);

        holder.articleCount.setText((position + 1) +" of " + (articles.size()));

// set on click listeners for all the controls body - title - image
        holder.desc.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            String strWebUrl = article.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            // parse through the url and open it
            Uri url = Uri.parse(strWebUrl);
            intent.setData(url);
            mainActivity.startActivity(intent);
        } });

        holder.title.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            String strWebUrl = article.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            // parse through the url and open it
            Uri url = Uri.parse(strWebUrl);
            intent.setData(url);
            mainActivity.startActivity(intent);
        } });

        holder.newsImage.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) {
            String strWebUrl = article.getUrl();
            Intent intent = new Intent(Intent.ACTION_VIEW);

            // parse through the url and open it
            Uri url = Uri.parse(strWebUrl);
            intent.setData(url);
            mainActivity.startActivity(intent);
        } });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
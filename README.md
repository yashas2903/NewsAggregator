# News Aggregator Application

## Overview

This Android application functions as a news aggregator, enabling users to access current news articles from diverse sources across various categories. The app employs the NewsAPI.org service to retrieve news sources and articles.

## Features

- Present news articles from a broad range of sources covering diverse news categories.
- Utilize components such as Drawer Layout, View Binding, ViewPager2, Adapters, Internet, APIs, Android Volley, Dynamic Menus, and Implied Intents.
- Facilitate the filtering of news sources by topic, allowing users to focus on relevant news articles.
- Enable users to navigate through articles by swiping right to move forward and left to go back.
- Provide access to complete extended articles on the respective news source's website by clicking on the article title, text, or image content.
- Incorporate a professionally designed launcher icon.
- Integration with NewsAPI.org for obtaining news source and article data.
- Implementation of Android Volley for API JSON downloads.
- Utilization of Android View Binding in all activities.
- Implement dynamic menu behavior based on the retrieved data.

## API Key

Users are required to obtain an API key from NewsAPI.org to access their services. Registration can be completed at [NewsAPI.org](https://newsapi.org/register) to acquire the necessary API key, which must be supplied with NewsAPI.org queries.

## Application Behavior Diagrams

### 1. Startup

- Open the Drawer and select a news source.
- Choose news topics using the options menu.
- Display news-related background before loading articles.

### 2. Selecting a Topic

- The options menu dynamically adjusts its content based on data retrieved from news sources.
- Selecting a topic reduces the content of the News Sources drawer-list.

### 3. Selecting a News Source

- Display the chosen news source in the Navigation Bar.
- Employ ViewPager2 for swipe navigation between articles.
- Indicate the article count at the bottom of the screen.

### 4. Swipe Right (or Left) to Scroll Through Articles

### 5. Click on Article Title, Image, or Text

- Direct users to the extended article on the news source's website.

## News Source Data Representation

- Maintain a full list of news sources unchanged from the API.
- Utilize a "Current" list of sources for display in the drawer.
- Update the "Current" source list based on selected topics.

## Extra Credits

- Convert news article date/time stamps to a user-friendly format.
- Implement saving and restoring of app state during orientation changes.
- Apply color coding to news topics in the options menu and news sources in the drawer.

## Usage

1. Clone the repository.
2. Open the project in Android Studio.
3. Acquire an API key from NewsAPI.org and replace `YOUR_API_KEY` in the code with your key.
4. Build and run the app on an Android emulator or device.

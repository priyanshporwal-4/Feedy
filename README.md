# Feedy - A Food Donation App

## Running the Application

### Software Requirement

`Android Studio Jellyfish | 2023.3.1 Canary 10`

### 1. Create API Key for Gemini
Visit [https://aistudio.google.com/app/apikey](https://aistudio.google.com/app/apikey) to create an API key for Gemini.

### 2. Create BuildConfig.java
Create a `BuildConfig.java` file in `Android : app/src/main/java/com/converter/feedy/`

```java
// File: BuildConfig.java

public class BuildConfig {
    public static String apiKey = "YOUR_GEMINI_API_KEY";
}
```

### 3. Integrate Firebase API Key

Now, move to `Android : app/src/main/AndroidManifest.xml` file.

Instead of `"Your_Api_Key"` in meta-data tag

```xml

<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="AIzaSyCu41tZOipO38CZVyGHsZcz9KfLWQDEFa" />
```

### 4. Please add the current point number at the end into API key which you had implemented in 3rd step

This step is added for security reasons

## Overview

Welcome to Feedy, a food donation app designed to contribute towards achieving the United Nations Sustainable Development Goal of No Hunger (SDG 2). Feedy facilitates the donation of surplus food from individuals and businesses to those in need, fostering a community-driven approach to reduce food waste and combat hunger.

## 1. Advantages Supporting SDG 2: No Hunger

### a. Food Waste Reduction

Feedy actively contributes to reducing food waste by connecting surplus food donors with local charities and individuals facing hunger. Through efficient donation channels, we strive to minimize the environmental impact of food waste.

### b. Addressing Food Insecurity

Our app directly addresses the issue of food insecurity by ensuring that excess food is redirected to those who need it the most. Feedy acts as a bridge between abundance and need, fostering a sense of community responsibility.

### c. Community Empowerment

Feedy empowers individuals and businesses to make a positive impact on their communities. By encouraging everyone to play a role in addressing hunger, we promote a collective effort towards achieving SDG 2.

### d. Sustainable Practices

The app promotes sustainable practices by encouraging the donation of surplus food instead of discarding it. Feedy aligns with the broader environmental goals by actively participating in the reduction of food-related environmental impact.

## 2. SDG 12: Responsible Consumption and Production

Our app promotes responsible consumption by encouraging the donation of surplus food instead of discarding it. Feedy aligns with the broader environmental goals by actively participating in the reduction of food-related environmental impact.

## 3. SDG 17: Partnerships for the Goals

Feedy fosters partnerships between food donors, charities, and individuals, creating a collaborative network to address hunger and reduce food waste.

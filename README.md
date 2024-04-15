Multi-account currency exchange app.

Libraries used:
  * Retrofit - making web requests
  * Hilt - DI + Singleton
  * Room - storing transactions data
  * Datastore - storing latest currency values and requests-related data

An app build with MVVM architecture. It uses Hilt to store and manage app-related data, such as currency values. 
Background service also in use to update currencies every 5 seconds or refresh it immediately.
Built without any relations to EUR or other currencies what gives an opportunity to delete, add or manage any currencies 
by changing them on back-end with no Android work required. App converts Any-to-Any currency by converting it 
back to base currency and transforming into required one. Modular, easy to navigate structure of the project gives 
an opportunity to modify different parts of it with no major changes. It also contains a class which handle all
of the current fee and transaction rules, so them also can be easily modified to fit requirements.

I also planned some unreleased features for that project before starting the work, but unfortunately I had a small amount of time 
during weekends to finish them, so as a result you may found some unused code such as failed transactions or selected currencies.
I also tried not to spent a lot of time on designs, so the images and colors may look not so suitable for such kind of project, 
but I tried my best :) UX should also handle all of the issues those UI cards creating, i.e. you can navigate to the currency you
have a transaction with by clicking on that transaction, and also the base currency will always be the first one in the list, 
while other currencies will sort by amount and name.

By the git it took around 15 hours to create this

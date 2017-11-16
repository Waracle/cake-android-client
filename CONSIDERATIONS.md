# Considerations of the Test


## Architecture

- Instead of using a typical MVP, MVVM, MVI, passive MVP or any other design pattern for the presentation
layer, having in consideration the ease of the presentation logic, I've chosen to split the project in
three different layers in order to abstract all the logic related to each of them (presentation, core and
data). Even without a proper Repository or Presenter, the project is structured in a clean way. This decision
makes easy to switch any of the android components used for the test for any other third party libraries in
any of the layers.

- Given the mandatory rule of not using third party libraries and I couldn't use Dagger, there is a custom Injector
made for the Fragment in order to provide all necessary dependencies.







### NOT COMPLETED


# Code

- I've been commiting and explaining the reason of every commit, so most of the explanations of
changes in the project are provided by the git history.

- I've done a clean up, including the code related to the refresh action in the ToolBar. With more time,
it would have been good implementing a Pull to refresh for the list and an empty/error state for the screen in case
there is no connectivity or the item list retrieved is empty.

- Even if we could have nested classes in the MainActivity for either the AsyncTask or the Fragment, it's better not
to do it since we could be introducing leaks to the app. We need to remember to use `static final` declarations in
case we wanted to leave nested classes as they were.


- Packaging organised by feature and then by model.


# CD & CI

- Even though in a real project it would be ideal using GitFlow branching model in order to work better as a team, the main project fixes have been done
in the master branch, and improvements have been done in feature branches, since the purpose of those has been tackling the tech debt in the project.

- Within the time I have got to do the test, I haven't got time to setup a Continuous Integration system, so I haven't use any service like Travis or
CircleCI (I usually use the last mentioned).
# Virgin Money Developer Challenge - Android

**VirginMoney** is an application that allows staff to
* See all of their colleagues contact details
* See which rooms in the office are currently occupied

Submitted by: **Mathurin Bloworlf**

## VirginMoney

The following **required** tasks are completed:

* [X] Create app using **Kotlin** and a minimum SDK **19**
  * [X] Implement most recent and recommended technology stack
* [X] Contains **list/detail** pages to display people
* [X] Contains **list** page to display rooms

The following **optional** features are implemented:

* [X] Persistent session using [Firebase](https://firebase.google.com/)
  * [X] User can log in with **email/password**
  * [X] User can log in with [**Google**](https://firebase.google.com/docs/auth/android/google-signin)
  * [X] User can log in with [**Facebook**](https://firebase.google.com/docs/auth/android/facebook-login)
* [X] Fetch displayed data from [given API](https://61e947967bc0550017bc61bf.mockapi.io/api/v1/)
  * [X] Endpoint for [people](https://61e947967bc0550017bc61bf.mockapi.io/api/v1/people)
  * [X] Endpoint for [rooms](https://61e947967bc0550017bc61bf.mockapi.io/api/v1/rooms)
* [X] Display data using **RecyclerView**, custom **RecyclerView Adapter** and **View Holder**
* [X] Using [Dependency Injection](https://developer.android.com/training/dependency-injection) with [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
* [X] Navigate through pages with [Navigation Component](https://developer.android.com/guide/navigation/get-started)

## Development Story

The following story around how the app will be used should inform how you approach development/code structure:

Virgin Money aim to use their branding in all of their internal services. They currently use a main brand color
`#C40202` however they are in the early stages of a rebrand that may lead this to change in the near future.

All employees will have access to the app and will expect the ability to quickly pull up and use the contact details of any of their colleagues. All details of the contact should be displayed in the app.Employees use Android devices across the full range, so your design must work across phones and tablets. Several of our employees use the accessibility features of Android, so your app **should** be accessibile.

If the trial of the Directory app proves successful with the staff, Virgin Money may look to expand the app so that it will also allow users to access and administer more data, so ensure that the app can be easily expanded both in terms of codebase and UX.  The code from this app could be used in other applications so modularity is **important**. If the app expands in scope, it will be more rigorously tested by our QA resource and will therefore **need** to support a test environment as well as a live environment.

Virgin Money cannot guarantee that the same developer(s) will always be working on this app throughout it's lifecycle, so it is important that other developers will be able to onboard themselves onto the codebase with ease.

## Screenshots

![Login Screen](http://url/to/img.png)](https://github.com/bloworlf/VirginMobile/blob/main/screens/login.png?raw=true)

![Sign up Screen](https://github.com/bloworlf/VirginMobile/blob/main/screens/signup.png?raw=true)

## Notes

-

## License

    Copyright 2018 Mathurin Bloworlf

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

package com.example.nicolas.vercoapp.activities;

//pruebas automatizadas de interfaz de usuario
import android.support.test.espresso.DataInteraction;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.nicolas.vercoapp.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.pressImeActionButton;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchProductActivityTest {

    @Rule
    public ActivityTestRule<SearchProductActivity> mActivityTestRule = new ActivityTestRule<>(SearchProductActivity.class);

    @Test
    public void searchProductActivityTest() {

        //ingresar busqueda.
        ViewInteraction appCompatImageView = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageView")), withContentDescription("Buscar"),
                        isDisplayed()));
        appCompatImageView.perform(click());


        ViewInteraction searchAutoComplete = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                        isDisplayed()));
        searchAutoComplete.perform(replaceText("zapatillas"), closeSoftKeyboard());

        ViewInteraction searchAutoComplete2 = onView(
                allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")), withText("zapatillas"),
                        isDisplayed()));
        searchAutoComplete2.perform(pressImeActionButton());

        //afirmaciones)->assertions

        ViewInteraction linearLayout = onView(allOf(withId(R.id.filtersLinearLayout), isDisplayed()));
        linearLayout.check(matches(isDisplayed()));

        ViewInteraction button = onView(allOf(withId(R.id.filterButton), isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(allOf(withId(R.id.filterButton), isDisplayed()));
        button2.check(matches(withText("FILTRAR")));

        ViewInteraction textView = onView(allOf(withId(R.id.selectedTypeTextView), isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(allOf(withId(R.id.selectedTypeTextView), isDisplayed()));
        textView2.check(matches(withText("MOSTRAR TODOS")));

        ViewInteraction recyclerView = onView(allOf(withId(R.id.productsRecyclerView), isDisplayed()));
        recyclerView.check(matches(isDisplayed()));

        //presiono boton de filtro
        ViewInteraction appCompatButton = onView(allOf(withId(R.id.filterButton), isDisplayed()));
        appCompatButton.perform(click());

        //interaccion con adapterview
        DataInteraction appCompatCheckedTextView = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)))
                .atPosition(0);
        appCompatCheckedTextView.perform(click());

        //assert
        ViewInteraction textView3 = onView(allOf(withId(R.id.selectedTypeTextView), isDisplayed()));
        textView3.check(matches(withText("HOMBRE")));

        //vuelvo a presionar el boton
        ViewInteraction appCompatButton2 = onView(allOf(withId(R.id.filterButton), isDisplayed()));
        appCompatButton2.perform(click());

        //interaccion con adapterview
        DataInteraction appCompatCheckedTextView2 = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)))
                .atPosition(1);
        appCompatCheckedTextView2.perform(click());

        //assert
        ViewInteraction textView4 = onView(allOf(withId(R.id.selectedTypeTextView), isDisplayed()));
        textView4.check(matches(withText("MUJER")));

        //vuelvo a presionar boton
        ViewInteraction appCompatButton3 = onView(allOf(withId(R.id.filterButton), isDisplayed()));
        appCompatButton3.perform(click());

        //interaccion con adapterview
        DataInteraction appCompatCheckedTextView3 = onData(anything())
                .inAdapterView(allOf(withClassName(is("com.android.internal.app.AlertController$RecycleListView")),
                        childAtPosition(
                                withClassName(is("android.widget.FrameLayout")),
                                0)))
                .atPosition(2);
        appCompatCheckedTextView3.perform(click());

        //assert
        ViewInteraction textView5 = onView(allOf(withId(R.id.selectedTypeTextView), isDisplayed()));
        textView5.check(matches(withText("MOSTRAR TODO")));

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

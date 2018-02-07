<#include "header.ftl">

<h1 id="page-head">Login</h1>

<form method="POST">
    <fieldset class="noborder">
        <div>
            <label>Username: </label>
            <input type="text"  id="username-field" name="login-username"></input>
            <button type="submit" class="action-button" id="login-button">Login</button>
        </div>
    </fieldset>
</form>

<#include "footer.ftl">
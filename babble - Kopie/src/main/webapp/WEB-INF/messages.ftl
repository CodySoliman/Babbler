<#include "header.ftl">

<h1 id="head-title">Konversation mit Benutzer @${RequestParameters.id}</h1>

<table id="message-box">
         <tr>
            <td <#if success> class="success"<#else> class="error"</#if>><#if message??>${message}</#if></td>
         </tr>
      </table>

<#if resultBabbleMessages??>
      <#list resultBabbleMessages as babble>
      <table id="babble-box-woi" class="babble-border">
         <tr>
            <td id="babble-creator"><#if babble.creatorUsername == Session.loggedInUserName>Du:<#else>${babble.creatorName} @${babble.creatorUsername}</#if></td>
         </tr>
         <tr>
            <td id="babble-text">${babble.text}</td>
         </tr>
         <tr>
            <td id="babble-count"></td>
         </tr>
         <tr>
            <td id="babble-time">${babble.formattedCreationTime}</td>
         </tr>
      </table>      
      </#list>
</#if>

<form method="POST">
    <fieldset class="noborder">
        <div>
            <textarea id="babble-creation-text" cols="86" rows="5" name="babbleMessage"></textarea>
        </div>
    </fieldset>
    <button type="submit" name="sendBabbleMessage" class="action-button" id="post-button">Send Message</button>
</form>


<#include "footer.ftl">
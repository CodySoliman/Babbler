<html>
<head>
    <title>${pagetitle}</title>
</head>
   <#if found>
   <body>
   <nav>
       <ul>
           <li><a href="babble_search">Search Babble</a></li>
           <li><a href="">${follow}</a></li>
           <li><a href="">${block}</a></li>
       </ul>
   </nav>
   <div id="userbox">
       <div>
           <table>
               <tr>
                   <td>Benutzer:</td>
                   <td>${user_username}</td>
               </tr>
               <tr>
                   <td>Name:</td>
                   <td>${user_name}</td>
               </tr>
               <tr>
                   <td>Status:</td>
                   <td>${user_status}</td>
               </tr>
           </table>
       </div>
       <div>
           <img id="profile_pic" src="${user_pic}"/>
       </div>
   </div>
      <#if babbles?has_content>
      <div>
         <#list babbles as babble>
             <table>
         <#if babble.interactionType != "optional">
            <tr>
                <td>@${user_username} hat ${babble.interactionType}</td>
            </tr>
         </#if>
                 <tr>
                     <td>${babble.creatorUsername}</td>
                 </tr>
                 <tr>
                     <td>${babble.text}</td>
                 </tr>

            <#if babble.interactionType != "optional">
            <tr>
                <td>${babble.interactionTime}</td>
            </tr>
            <#else>
            <tr>
                <td>${babble.creationTime}</td>
            </tr>
            </#if>
             </table>
         </#list>
      </div>
      </#if>
   </body>
   </#if>
</html>
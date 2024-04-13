function getAgencies()
{
  const agencyFileUrl = ("https://dev.eliotindex.org/agencies.txt");
  const agencyCodes = [];
  const agencyNames = [];

  fetch(agencyFileUrl)
    .then(r => r.text())
    .then((text) => {
      const agencyFile = text.split("\n");
      agencyFile.pop();

      for (let i = 0; i < agencyFile.length; i++)
      {
        var data = agencyFile[i];
        agencyCodes.push(data.substring(0, data.indexOf(";")));
        data = data.substr(data.indexOf(";") + 1);
        agencyNames.push(data.substring(0, data.indexOf(";")));
        document.getElementById("agencies").innerHTML += ("<a href=#" + agencyCodes[i] + ">" + agencyNames[i] + "</a> (" + agencyCodes[i] + ")<br>");
      }
    })
}
function getAgencies2() // for stops
{
  const agencyFileUrl = ("https://dev.eliotindex.org/agencies.txt");
  const agencyCodes = [];
  const agencyNames = [];

  fetch(agencyFileUrl)
    .then(r => r.text())
    .then((text) => {
      const agencyFile = text.split("\n");
      agencyFile.pop();

      for (let i = 0; i < agencyFile.length; i++)
      {
        var data = agencyFile[i];
        agencyCodes.push(data.substring(0, data.indexOf(";")));
        data = data.substr(data.indexOf(";") + 1);
        agencyNames.push(data.substring(0, data.indexOf(";")));
        document.getElementById("agencies").innerHTML += ("<a href=stops/" + agencyCodes[i] + ".html>" + agencyNames[i] + "</a> (" + agencyCodes[i] + ")<br>");
      }
    })
}
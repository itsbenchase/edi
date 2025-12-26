const agencies = [];
const fullAgencies = [];
const agencyLines = [];

const routeCodes = [];
const routeLengths = [];
const routeEdis = [];
const routeSpacings = [];

function getAgencies()
{
  const agencyFileUrl = ("https://dev.eliotindex.org/agencies.txt"); // provide file location
  fetch(agencyFileUrl)
    .then(r => r.text())
    .then((text) => {
      const agencyFile = text.split("\n");
      agencyFile.pop();

      for (let i = 0; i < agencyFile.length; i++)
      {
          var data = agencyFile[i];
          agencies.push(data.substring(0, data.indexOf(";")));
          data = data.substr(data.indexOf(";") + 1);
          fullAgencies.push(data);
          agencyLines.push(fullAgencies[i] + " (" + agencies[i] + ")");
          document.getElementById("agencyDrop").innerHTML += ("<option value=" + agencies[i] + ">" + agencyLines[i] + "</option>");
      }
      })
}

function getRoutes()
{
  document.getElementById("listing").innerHTML = ("");

  routeCodes.length = 0;
  routeLengths.length = 0;
  routeEdis.length = 0;
  routeSpacings.length = 0;

  var selectedAgency = document.querySelector('#agencyDrop');
  var agency = selectedAgency.value;

  const routeFileUrl = ("https://dev.eliotindex.org/edis/" + agency + ".txt"); // provide file location
  fetch(routeFileUrl)
    .then(r => r.text())
    .then((text) => {
        const routeFile = text.split("\n");
        routeFile.pop();

        for (let i = 0; i < routeFile.length; i++)
        {
          var data = routeFile[i];
          routeCodes.push(data.substring(0, data.indexOf(";")));
          data = data.substr(data.indexOf(";") + 1);
          routeLengths.push(data.substring(0, data.indexOf(";")));
          data = data.substr(data.indexOf(";") + 1);
          routeEdis.push(data.substring(0, data.indexOf(";")));
          data = data.substr(data.indexOf(";") + 1);
          routeSpacings.push(data.substring(0, data.indexOf(";")));
        }

        fillTable();
    })
}

function fillTable()
{
  document.getElementById("listing").innerHTML += ("<tr><th>Route Code</th><th>Line Length</th><th>Eliot Deviation Index</th><th>Stop Spacing</th></tr> \n")

  for (let i = 0; i < routeCodes.length; i++)
  {
    document.getElementById("listing").innerHTML += ("<tr><td style=color:#ff0000>" + routeCodes[i] + "</td><td>" + routeLengths[i] + " mi.</td><td>" + routeEdis[i] + "</td><td>" + routeSpacings[i] + " mi.</td></tr> \n") 
  }

  getStats();
}

function getStats()
{
  // find averages
  // apparently this fixes the number sorting issues
  var medianEdi = 0.00;
  var medianLength = 0.00;
  var medianSpacings = 0.00;
  var totalMiles = 0.00;

  // we need copies
  const lineEdis2 = [...routeEdis];
  const lineLengths2 = [...routeLengths];
  const lineSpacings2 = [...routeSpacings];

  lineEdis2.sort(function(a, b){return a - b;});
  lineLengths2.sort(function(a, b){return a - b;});
  lineSpacings2.sort(function(a, b){return a - b;});

  // total miles
  for (let i = 0; i < lineLengths2.length; i++)
  {
    totalMiles += parseFloat(lineLengths2[i]);
  }
  totalMiles = Math.round(totalMiles * 100) / 100;
  document.getElementById("agencyMiles").innerHTML = (totalMiles + " mi.");

  // median EDI
  if (lineEdis2.length % 2 == 1) // odd amount of EDIs
  {
    if (lineEdis2.length == 1)
    {
      medianEdi = lineEdis2[0];
    }
    else
    {
      medianEdi = lineEdis2[Math.round(lineEdis2.length / 2) - 1];
    }
    document.getElementById("medianEdi").innerHTML = (medianEdi);
  }
  else // even amount of EDIs
  {
    if (lineEdis2.length == 2)
    {
      var val1 = parseFloat(lineEdis2[0]);
      var val2 = parseFloat(lineEdis2[1]);
      medianEdi = (val1 + val2) / 2;
    }
    else
    {
      var val1 = parseFloat(lineEdis2[(lineEdis2.length / 2) - 1]);
      var val2 = parseFloat(lineEdis2[(lineEdis2.length / 2)]);
      medianEdi = (val1 + val2) / 2;
    }
    medianEdi = Math.round(medianEdi * 100) / 100;
    document.getElementById("medianEdi").innerHTML = (medianEdi);
  }

  // median spacing
  if (lineSpacings2.length % 2 == 1) // odd amount of spacings
  {
    if (lineSpacings2.length == 1)
    {
      medianSpacings = lineSpacings2[0];
    }
    else
    {
      medianSpacings = lineSpacings2[Math.round(lineSpacings2.length / 2) - 1];
    }
    document.getElementById("medianSpacing").innerHTML = (medianSpacings);
  }
  else // even amount of spacings
  {
    if (lineEdis2.length == 2)
    {
      var val1 = parseFloat(lineSpacings2[0]);
      var val2 = parseFloat(lineSpacings2[1]);
      medianSpacings = (val1 + val2) / 2;
    }
    else
    {
      var val1 = parseFloat(lineSpacings2[(lineSpacings2.length / 2) - 1]);
      var val2 = parseFloat(lineSpacings2[(lineSpacings2.length / 2)]);
      medianSpacings = (val1 + val2) / 2;
    }
    medianSpacings = Math.round(medianSpacings * 100) / 100;
    document.getElementById("medianSpacing").innerHTML = (medianSpacings + " mi.");
  }

  // median length
  if (lineLengths2.length % 2 == 1) // odd amount of EDIs
  {
    if (lineLengths2.length == 1)
    {
      medianLength = lineLengths2[0];
    }
    else
    {
      medianLength = lineLengths2[Math.round(lineLengths2.length / 2) - 1];
    }
    document.getElementById("medianLength").innerHTML = (medianLength + " mi.");
  }
  else // even amount of EDIs
  {
    if (lineLengths2.length == 2)
    {
      var val1 = parseFloat(lineLengths2[0]);
      var val2 = parseFloat(lineLengths2[1]);
      medianLength = (val1 + val2) / 2;
    }
    else
    {
      var val1 = parseFloat(lineLengths2[(lineLengths2.length / 2) - 1]);
      var val2 = parseFloat(lineLengths2[(lineLengths2.length / 2)]);
      medianLength = (val1 + val2) / 2;
    }
    medianLength = Math.round(medianLength * 100) / 100;
    document.getElementById("medianLength").innerHTML = (medianLength + " mi.");
  }

  // all table display
  document.getElementById("agencyRoutes").innerHTML = (lineEdis2.length);
}
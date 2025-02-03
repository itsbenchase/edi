  function randRoute()
  {
    const randAgencyFileUrl = ("https://dev.eliotindex.org/agencies.txt"); // provide file location
    fetch(randAgencyFileUrl)
        .then(r => r.text())
        .then((text) => {
          const randAgencyFile = text.split("\n");
          randAgencyFile.pop();

          const randAgencies = [];
          const fullAgencies = [];

          for (let i = 0; i < randAgencyFile.length; i++)
          {
            var data = randAgencyFile[i];
            randAgencies.push(data.substring(0, data.indexOf(";")));
            data = data.substr(data.indexOf(";") + 1);
            fullAgencies.push(data);
          }

          var randAgency = Math.floor(Math.random() * randAgencyFile.length);
          var agency = randAgencies[randAgency];
          var fullAgency = fullAgencies[randAgency];

          document.getElementById("randAgency").innerHTML = (fullAgency + " (" + agency + ")");

          const randListFileUrl = ("https://dev.eliotindex.org/edis/" + agency +  ".txt"); // provide file location
          fetch(randListFileUrl)
            .then(r => r.text())
            .then((text) => {
              const randListFile = text.split("\n");
              randListFile.pop();

              const randNames = [];
              const randBranches = [];
              const randLengths = [];
              const randEdis = [];

              for (let i = 0; i < randListFile.length; i++)
              {
                var data = randListFile[i];
                data = data.substr(data.indexOf(";") + 1);
                randLengths.push(data.substring(0, data.indexOf(";")));
                data = data.substr(data.indexOf(";") + 1);
                randEdis.push(data.substring(0, data.indexOf(";")));
                data = data.substr(data.indexOf(";") + 1);
                randNames.push(data.substring(0, data.indexOf(";")));
                data = data.substr(data.indexOf(";") + 1);
                randBranches.push(data);
              }

              var randName = Math.floor(Math.random() * randListFile.length);
              var name = randNames[randName];
              var branch = randBranches[randName];
              var length = randLengths[randName];
              var edi = randEdis[randName];

              document.getElementById("randName").innerHTML = (name + " (" + branch + ")");
              document.getElementById("randLength").innerHTML = (length + " miles");
              document.getElementById("randEdi").innerHTML = (edi);
            })
      })
  }
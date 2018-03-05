#!/bin/sh
#Muuntaa tiedostojen merkistön
usage()
{
	echo NAME
	echo "     " muunna merkit - muuntaa tekstin merkiston
	echo ""
	echo SYNOPSIS
	echo "     " ./muunna_merkit.sh [--no-backup] [merkistosta] [merkistoon] [tiedostojennimet]
	echo ""
	echo OPTIONS
	echo "     " --no-backup ei luo varmuuskopiota
}

# Siivoaa jäljet, eli tuhoaa temppitiedoston, jonka jälkeen
# tulostaa tarvittaessa käyttöohjeet
cleanup()
{
rm "$temppitiedosto"
if [[ "$1" == "1" ]]
	then usage
fi
exit 1
}

# Luo temppitiedoston, johon tiedot kirjotetaan väliaikaisesti
luo_temppi()
{
temppitiedosto=`mktemp`
if [ "$?" -ne "0" ]
then
    echo Väliaikaistiedostoa ei voitu luoda. Lopetan.
    exit 1
fi

trap cleanup SIGHUP SIGINT SIGQUIT SIGABRT
}

# Tarkistaa halutaanko tehdä backup, toimii sen mukaisesti
# ja kutsuu itse muuntamisohjelmaa, joka kirjoittaa temppitiedostoon
# joka taas kopioidaan alkuperäisen päälle, jos kaikki on ok
muunna()
{
	if [[ "$4" == "0" ]]
	then
		# tarkistetaan onko backuptiedostoa olemassa
		if [[ ! -w "$3.alkup" ]]
			then 
			mv $3 $3.alkup
		else cleanup 1
		fi
	fi
	if [ "$?" -ne "0" ]
		then cleanup 1
	fi
	
	iconv -f $1 -t $2 $3.alkup > "$temppitiedosto"
	if [ "$?" -ne "0" ]
		then cleanup 1
	fi
	cp "$temppitiedosto" $3
	
}
# Luodaan tarvittavat muutujat
backup=0
merkistosta=""
merkistoon=""
temppitiedosto=""

# Luodaan temppitiedosto myöhempää käyttöä varten
luo_temppi


while [ "$#" -gt "0" ]
do
	# Tarkistetaan onko ensimmäinen argumentti --help
	if [[ "$1" == "--help" ]] 
		then usage
	# Tarkistetaan halutaanko backuppia
	elif [[ "$1" == "--no-backup" ]] 
		then backup=1
	else
		# Otetaan halutut merkistöt talteen
		merkistosta=$1
		shift
		merkistoon=$1
		shift
		
		# Kutsutaan itse muuntoaliohjelmaa niin kauan kun parametrejä riittää
		while [ "$#" -gt "0" ]
		do
			muunna $merkistosta $merkistoon $1 $backup
			shift
		done
	fi
	shift
done
cleanup
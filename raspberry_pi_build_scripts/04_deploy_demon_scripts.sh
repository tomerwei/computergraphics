# Copy local scripts to /usr/local/bin so they can be used in
# a daemon.
echo "Init script"
sudo cp scripts/homeautomation /etc/init.d
echo "Copying start script"
sudo cp scripts/homeautomation_start.sh /usr/local/bin/ 
echo "Copying stop script"
sudo cp scripts/homeautomation_stop.sh /usr/local/bin/ 

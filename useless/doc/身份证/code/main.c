#include <stdint.h>
#include <stdio.h>
#include <ctype.h>
#include <string.h>
#include <stdarg.h>
#include <errno.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include "posapi.h"
#include "iso14443.h"
#include "hexdump.h"

int main(int argc, char **argv)
{
 	int fd;
	int retval;
    uint8_t atqb_len;
    uint8_t atqb[64];
    uint8_t attrib_len;
    uint8_t attrib[64];
    
    uint8_t cid = 0x08;     // CID
    uint8_t fsdi = 0x08;    // 256 buffer
    uint8_t BRit = 0x00;    // Baud rate 106kbps
    uint8_t BRti = 0x00;    // Baud rate 106kbps
    
    const uint8_t readfile[] = {0x80, 0xB0, 0x00, 0x00, 0x20}; 
    const uint8_t uidcmd[] = {0x00, 0x36, 0x00, 0x00, 0x08};        
	uint8_t txbuf[256]  = {0x00, 0xA4, 0x00, 0x00, 0x02, 0x60, 0x02};
	uint8_t rxbuf[256];
	uint32_t txlen;
	uint32_t rxlen;
    uint32_t timeout = 5000;
    
	printf("Identification Card Test\n");

	/* OPEN */
	printf("Open: processing...\n");
	fd = mif_open("/dev/nfc");
	if (fd < 0) {
		printf("Open: failed erron:%d, %s\n", errno, strerror(errno));
		goto exit_entry;
	}

    /* RESET */
    iso14443_reset_picc(fd);
    usleep(6*1000);
    
	retval = mif_select_carrier_type(fd, MIF_TYPE_B);
	if(retval<0) {
		printf("mif_select_carrier_type ret:%d errno:%d\n", retval, errno);
		goto close_exit_entry;
	}
	
	retval = iso14443_WupB(fd,0x00,0x00, &atqb_len, atqb);
	if (retval) {
        printf("iso14443_WupB ret:%d errno:%d\n", retval, errno);
		goto close_exit_entry;
	}
    printf("ATQB:\n");
    hexdump(atqb, atqb_len);
	
	retval = iso14443_AttriB(fd, cid, fsdi, &atqb[1], (BRit<<4) | BRti, &attrib_len, attrib);
    if (retval) {
        printf("iso14443_AttriB ret:%d errno:%d\n", retval, errno);
		goto close_exit_entry;
	}
    printf("ATTRIB:\n");
    hexdump(attrib, attrib_len);

    /* APDU */
    txlen = sizeof(uidcmd);
    retval = iso14443_no_tcl_exchange(fd, txlen, uidcmd, &rxlen, rxbuf, timeout);
    if (retval) {
        printf("iso14443_no_tcl_exchange send uid cmd ret:%d errno:%d\n", retval, errno);
		goto close_exit_entry;
	}
    printf("txbuf:\n");
    hexdump(uidcmd, txlen);
    printf("rxbuf:\n");
    hexdump(rxbuf, rxlen);
    
    txlen = 0x07;
    retval = iso14443_no_tcl_exchange(fd, txlen, txbuf, &rxlen, rxbuf, timeout);
    if (retval) {
        printf("iso14443_no_tcl_exchange ret:%d errno:%d\n", retval, errno);
		goto close_exit_entry;
	}
    printf("txbuf:\n");
    hexdump(txbuf, txlen);
    printf("rxbuf:\n");
    hexdump(rxbuf, rxlen);

    txlen = sizeof(readfile);
    retval = iso14443_no_tcl_exchange(fd, txlen, readfile, &rxlen, rxbuf, timeout);
    if (retval) {
        printf("iso14443_no_tcl_exchange send uid cmd ret:%d errno:%d\n", retval, errno);
		goto close_exit_entry;
	}
    printf("txbuf:\n");
    hexdump(readfile, txlen);
    printf("rxbuf:\n");
    hexdump(rxbuf, rxlen);
    
close_exit_entry:
	/* CLOSE */
	close(fd);
    
exit_entry:
    return 0;
}

